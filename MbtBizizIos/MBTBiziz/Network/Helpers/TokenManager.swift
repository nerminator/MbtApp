//
//  TokenManager.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import MSAL

enum AuthEnvironment {
    case dev
    case test
    case production

    private static let devBypassToken = "mbtbiziz-local-dev-bypass-token"

    static var current: AuthEnvironment {
        let executableName = Bundle.main.object(forInfoDictionaryKey: "CFBundleExecutable") as? String
        let bundleIdentifier = Bundle.main.bundleIdentifier

        let isDevTarget = (executableName?.contains("-Dev") == true) || (bundleIdentifier?.hasSuffix(".dev") == true)
        if isDevTarget {
            return .dev
        }

        let isProdTarget = (executableName?.contains("-Prod") == true) || (executableName?.contains("-Production") == true) || (bundleIdentifier == "com.daimlertruck.dtag.internal.ios.mbt.test")
        return isProdTarget ? .production : .test
    }

    var isOidcBypassed: Bool {
        switch self {
        case .dev:
            return true
        case .test, .production:
            return false
        }
    }

    var bypassAccessToken: String {
        switch self {
        case .dev:
            return AuthEnvironment.devBypassToken
        case .test, .production:
            return ""
        }
    }

    // ✅ Client IDs
    var clientId: String {
        switch self {
        case .dev:
            return ""
        case .production:
            return "41b14095-c137-4f5d-b300-accd37cbb4aa"
        case .test:
            return "e97a3881-d802-433b-8529-96e0e801e346"
        }
    }

    var authority: String {
        switch self {
        case .dev:
            return ""
        case .production:
            return "https://login.microsoftonline.com/505cca53-5750-4134-9501-8d52d5df3cd1"
        case .test:
            return "https://login.microsoftonline.com/505cca53-5750-4134-9501-8d52d5df3cd1"
        }
    }

    // ✅ Redirect URIs based on your bundle ids
    var redirectUri: String {
        switch self {
        case .dev:
            return ""
        case .production:
            return "msauth.com.daimlertruck.dtag.internal.ios.mbt.test://auth"
        case .test:
            return "msauth.com.daimlertruck.dtag.internal.ios.mbt.test2://auth"
        }
    }

    // ✅ Scopes
    var scopes: [String] {
        switch self {
        case .dev:
            return []
        case .production:
            return ["api://48252d22-0987-4d84-b1d9-00468ec9d424/Read"]
        case .test:
            return ["api://910155c2-0cc9-4d21-a48b-4c49c99f8128/Read"]
        }
    }

    // ✅ Optional: keep Keychain tokens isolated per environment
    var keychainService: String {
        switch self {
        case .dev: return "com.mbtbiziz.auth.dev"
        case .production: return "com.mbtbiziz.auth"
        case .test: return "com.mbtbiziz.auth.test2"
        }
    }
}

class TokenManager {
    
    static let account = "access_token"
    private let environment = AuthEnvironment.current

    static var service: String {
        return AuthEnvironment.current.keychainService
    }
    
    static func save(token: String) {
        KeychainHelper.save(token, service: service, account: account)
    }

    static func getToken() -> String? {
        return KeychainHelper.read(service: service, account: account)
    }

    static func clear() {
        KeychainHelper.delete(service: service, account: account)
    }
    
    static let _sharedManager = TokenManager()
    
    static var sharedManager : TokenManager {
        if _sharedManager.applicationContext == nil{
            do {
                try _sharedManager.initMSAL()
            } catch let error {
                print ("Unable to create Application Context \(error)")
            }
        }
        return _sharedManager
    }
    
    private var kClientID: String { environment.clientId }
    private var kRedirectUri: String { environment.redirectUri }
    private var kScopes: [String] { environment.scopes }
    private var kAuthority: String { environment.authority } // base authority URL
    let kGraphEndpoint = "https://graph.microsoft.com/"

    
    var applicationContext : MSALPublicClientApplication?
    var webViewParamaters : MSALWebviewParameters?
    
    var currentAccount: MSALAccount?
    var currentDeviceMode: MSALDeviceMode?
    
    var accessToken : String {
        return _accessToken ?? ""
    }
    
    fileprivate var _accessToken: String? {
        get {
            return KeychainHelper.read(
                service: TokenManager.service,
                account: TokenManager.account
            )
        }
        set {
            if let token = newValue {
                KeychainHelper.save(
                    token,
                    service: TokenManager.service,
                    account: TokenManager.account
                )
            } else {
                KeychainHelper.delete(
                    service: TokenManager.service,
                    account: TokenManager.account
                )
            }
        }
    }
    
    
  /*  var token : String {
        return _token ?? ""
    }
    fileprivate var _token : String? = UserDefaults.standard.object(forKey: MBTConstants.UserPreference.Token) as? String {
        didSet {
            UserDefaults.standard.set(_token, forKey: MBTConstants.UserPreference.Token)
        }
    }
    
    func clearToken() {
        _token = nil
    }
    */
    func clearAccessToken() {
        _accessToken = nil
    }
    
    func isTokenInvalid() -> Bool {
        return false
    }
  
    /*
    func handleLoginResponse(_ loginResponse : MBTLoginResponse) {
        guard let token = loginResponse.token else { return }
        _token = token
    }
    */
    
    func initMSAL() throws {
        if environment.isOidcBypassed {
            self._accessToken = environment.bypassAccessToken
            return
        }

        guard let authorityURL = URL(string: kAuthority) else { return }
        let authority = try MSALAADAuthority(url: authorityURL)

        let msalConfiguration = MSALPublicClientApplicationConfig(
            clientId: kClientID,
            redirectUri: kRedirectUri,
            authority: authority
        )

        self.applicationContext = try MSALPublicClientApplication(configuration: msalConfiguration)
        self.refreshDeviceMode()
    }
    
    
    func initWebViewParams(_ parentVc:UIViewController) {
        self.webViewParamaters = MSALWebviewParameters(authPresentationViewController: parentVc)
    }
    
    func signinSilently(_ completion:@escaping (_ success : Bool)->()) {
        if environment.isOidcBypassed {
            self._accessToken = environment.bypassAccessToken
            completion(true)
            return
        }
        
        self.loadCurrentAccount { (account) in
            if let currentAccount = account  {
                self.acquireTokenSilently(currentAccount, completion)
            } else {
                // We check to see if we have a current logged in account.
                // If we don't, then we need to sign someone in.
                // self.acquireTokenInteractively()
                completion(false)
            }
        }
    }
    
    func getShuttleOptions(_ completion:@escaping (_ response : MBTTransportationOptionsResponse?)->()) {
        WSProvider.shared.wsRequest(.getShuttleOptions, map: completion)
    }
    
    func acquireTokenInteractively (_ vc:UIViewController ,_ completion:@escaping (_ success : Bool, _ errorText : String?)->()) {
        if environment.isOidcBypassed {
            self._accessToken = environment.bypassAccessToken
            completion(true, nil)
            return
        }
        
        guard let applicationContext = self.applicationContext else {
            completion(false, "OIDC not initialized. Restart the app and try again.")
            return
        }
        
        self.initWebViewParams(vc)
        guard let webViewParameters = self.webViewParamaters else {
            completion(false, "OIDC web view could not be initialized.")
            return
        }

        let parameters = MSALInteractiveTokenParameters(scopes: kScopes, webviewParameters: webViewParameters)
        parameters.promptType = .selectAccount
        
        applicationContext.acquireToken(with: parameters) { (result, error) in
            
            if let error = error {
                let nsError = error as NSError
                let userInfoDetails = nsError.userInfo
                    .map { "\($0.key)=\($0.value)" }
                    .joined(separator: " | ")
                let details = userInfoDetails.isEmpty
                    ? "\(error.localizedDescription) [\(nsError.domain):\(nsError.code)]"
                    : "\(error.localizedDescription) [\(nsError.domain):\(nsError.code)] | \(userInfoDetails)"
                completion(false, details)
                return
            }
            
            guard let result = result else {
                completion(false, "OIDC result is empty.")
                return
            }
            
            self._accessToken = result.accessToken
            self.updateCurrentAccount(account: result.account)
            completion(true,nil)
        }
    }
    
    func acquireTokenSilently(_ account : MSALAccount!, _ completion:@escaping (_ success : Bool)->()) {
        if environment.isOidcBypassed {
            self._accessToken = environment.bypassAccessToken
            completion(true)
            return
        }
        
        guard let applicationContext = self.applicationContext else { return }
        
        /**
         
         Acquire a token for an existing account silently
         
         - forScopes:           Permissions you want included in the access token received
         in the result in the completionBlock. Not all scopes are
         guaranteed to be included in the access token returned.
         - account:             An account object that we retrieved from the application object before that the
         authentication flow will be locked down to.
         - completionBlock:     The completion block that will be called when the authentication
         flow completes, or encounters an error.
         */
        
        let parameters = MSALSilentTokenParameters(scopes: kScopes, account: account)
        
        applicationContext.acquireTokenSilent(with: parameters) { (result, error) in
            
            if let error = error {
                
                let nsError = error as NSError
                
                // interactionRequired means we need to ask the user to sign-in. This usually happens
                // when the user's Refresh Token is expired or if the user has changed their password
                // among other possible reasons.
                
                if (nsError.domain == MSALErrorDomain) {
                    
                    if (nsError.code == MSALError.interactionRequired.rawValue) {
                        DispatchQueue.main.async {
                            completion(false)
                        }
                    }
                }
                
                DispatchQueue.main.async {
                    completion(false)
                }
            }
            
            if let result = result  {
                
                self._accessToken  = result.accessToken
                
                DispatchQueue.main.async {
                    completion(true)
                }
                
            } else {
                DispatchQueue.main.async {
                    completion(false)
                }
            }
            
            
        }
    }
    
    func getGraphEndpoint() -> String {
        return kGraphEndpoint.hasSuffix("/") ? (kGraphEndpoint + "v1.0/me/") : (kGraphEndpoint + "/v1.0/me/");
    }
    

    
    typealias AccountCompletion = (MSALAccount?) -> Void

    func loadCurrentAccount(completion: AccountCompletion? = nil) {
        if environment.isOidcBypassed {
            self._accessToken = environment.bypassAccessToken
            if let completion = completion {
                completion(nil)
            }
            return
        }
        
        guard let applicationContext = self.applicationContext else { return }
        
        let msalParameters = MSALParameters()
        msalParameters.completionBlockQueue = DispatchQueue.main
                
        // Note that this sample showcases an app that signs in a single account at a time
        // If you're building a more complex app that signs in multiple accounts at the same time, you'll need to use a different account retrieval API that specifies account identifier
        // For example, see "accountsFromDeviceForParameters:completionBlock:" - https://azuread.github.io/microsoft-authentication-library-for-objc/Classes/MSALPublicClientApplication.html#/c:objc(cs)MSALPublicClientApplication(im)accountsFromDeviceForParameters:completionBlock:
        applicationContext.getCurrentAccount(with: msalParameters, completionBlock: { (currentAccount, previousAccount, error) in
            
            if let error = error {
                //"Couldn't query current account with error: \(error)")
                if let completion = completion {
                    completion(nil)
                }
            }
            
            if let currentAccount = currentAccount {
                
                //"Found a signed in account \(String(describing: currentAccount.username)). Updating data for that account...")
                
                self.updateCurrentAccount(account: currentAccount)
                
                if let completion = completion {
                    completion(self.currentAccount)
                }
                
                return
            }
            
            // If testing with Microsoft's shared device mode, see the account that has been signed out from another app. More details here:
            // https://docs.microsoft.com/en-us/azure/active-directory/develop/msal-ios-shared-devices
            if let previousAccount = previousAccount {
                
                // "The account with username \(String(describing: previousAccount.username)) has been signed out.")
                
            } else {
                
                //"Account signed out. Updating UX")
            }
            
            self._accessToken = ""
            self.updateCurrentAccount(account: nil)
            
            if let completion = completion {
                completion(nil)
            }
        })
    }
    
    /**
     This action will invoke the remove account APIs to clear the token cache
     to sign out a user from this application.
     */
    func signOut(parentVC:UIViewController) {
        if environment.isOidcBypassed {
            self._accessToken = nil
            self.updateCurrentAccount(account: nil)
            return
        }
        
        guard let applicationContext = self.applicationContext else { return }
        guard let account = self.currentAccount else { return }
        
        initWebViewParams(parentVC)
        
        do {
            
            /**
             Removes all tokens from the cache for this application for the provided account
             
             - account:    The account to remove from the cache
             */
            
            let signoutParameters = MSALSignoutParameters(webviewParameters: self.webViewParamaters!)
            
            // If testing with Microsoft's shared device mode, trigger signout from browser. More details here:
            // https://docs.microsoft.com/en-us/azure/active-directory/develop/msal-ios-shared-devices
            
            if (self.currentDeviceMode == .shared) {
                signoutParameters.signoutFromBrowser = true
            } else {
                signoutParameters.signoutFromBrowser = false
            }
            
            applicationContext.signout(with: account, signoutParameters: signoutParameters, completionBlock: {(success, error) in
                
                if let error = error {
                    //Couldn't sign out account with error: \(error)")
                    return
                }
                
                //Sign out completed successfully")
                self._accessToken = ""
                self.updateCurrentAccount(account: nil)
            })
            
        }
    }
    func updateCurrentAccount(account: MSALAccount?) {
        self.currentAccount = account
    }
    func refreshDeviceMode() {
        
        if #available(iOS 13.0, *) {
            self.applicationContext?.getDeviceInformation(with: nil, completionBlock: { (deviceInformation, error) in
                
                guard let deviceInfo = deviceInformation else {
                    return
                }
                
                self.currentDeviceMode = deviceInfo.deviceMode
            })
        }
    }
}
