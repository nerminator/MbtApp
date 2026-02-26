//
//  TokenManager.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import MSAL

class TokenManager {
    
    static let service = "com.mbtbiziz.auth"
    static let account = "access_token"
    
    
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
    let kClientID = "41b14095-c137-4f5d-b300-accd37cbb4aa"
    let kGraphEndpoint = "https://graph.microsoft.com/"
    let kAuthority = "https://login.microsoftonline.com/505cca53-5750-4134-9501-8d52d5df3cd1/oauth2/v2.0/authorize"
    let kRedirectUri = "msauth.com.daimlertruck.dtag.internal.ios.mbt.test://auth"

    let kScopes: [String] = ["api://48252d22-0987-4d84-b1d9-00468ec9d424/Read"]
    
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
        
        guard let authorityURL = URL(string: kAuthority) else {
            return
        }
        let authority = try MSALAADAuthority(url: authorityURL)
        let msalConfiguration = MSALPublicClientApplicationConfig(clientId: kClientID,
                                                                  redirectUri: kRedirectUri,
                                                                  authority: authority)
        self.applicationContext = try MSALPublicClientApplication(configuration: msalConfiguration)
        self.refreshDeviceMode()
    }
    
    func initWebViewParams(_ parentVc:UIViewController) {
        self.webViewParamaters = MSALWebviewParameters(authPresentationViewController: parentVc)
    }
    
    func signinSilently(_ completion:@escaping (_ success : Bool)->()) {
        
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
        
        guard let applicationContext = self.applicationContext else { return }
        
        self.initWebViewParams(vc)
        guard let webViewParameters = self.webViewParamaters else { return }

        let parameters = MSALInteractiveTokenParameters(scopes: kScopes, webviewParameters: webViewParameters)
        parameters.promptType = .selectAccount
        
        applicationContext.acquireToken(with: parameters) { (result, error) in
            
            if let error = error {
                //completion(false, "Could not acquire token: \(error)")
                completion(false, "Could not sign in")
                return
            }
            
            guard let result = result else {
                completion(false,"Could not sign in!")
                return
            }
            
            self._accessToken = result.accessToken
            self.updateCurrentAccount(account: result.account)
            completion(true,nil)
        }
    }
    
    func acquireTokenSilently(_ account : MSALAccount!, _ completion:@escaping (_ success : Bool)->()) {
        
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
