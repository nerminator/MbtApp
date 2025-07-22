//
//  WSProvider+fileprivate.swift
//
//  Created by Serkut Yegin.
//

import Moya
import Result
import Alamofire
import Marshal



#if MOCK
    fileprivate let SHOULD_STUB_RESPONSES = true
#else
    fileprivate let SHOULD_STUB_RESPONSES = false
#endif

typealias WSFailureCompletion = (WSError) -> Void
typealias MBTCancellable = Cancellable

fileprivate var additionalInfos : [String:Any] {
    let infoDict = [String:Any]()
    return infoDict
}

fileprivate let endpointClosure = { (target: NetworkAPI) -> Endpoint<NetworkAPI> in
    let urlString = URL(target: target).absoluteString
    return Endpoint(
        url: urlString.removingPercentEncoding ?? urlString,
        sampleResponseClosure: { .networkResponse(200, target.sampleData) },
        method: target.method,
        task: target.task,
        httpHeaderFields: target.headers
    )
}

fileprivate let stubClosure = { (target: NetworkAPI) -> Moya.StubBehavior in
    if !SHOULD_STUB_RESPONSES { return .never }
    return target.sampleData.isEmpty ? .never : .delayed(seconds: 1)
}

fileprivate let networkActivityPlugin = NetworkActivityPlugin { (type,target)  in
    switch type {
    case .began:
        NetworkActivityManager.sharedManager.pushNetworkActivity(target as! Loading)
        break
    case .ended:
        NetworkActivityManager.sharedManager.popNetworkActivity(target as! Loading)
        break
    }
}

fileprivate let policies: [String: ServerTrustPolicy] = [
   /* "bizizapp.com": .pinPublicKeys(
        publicKeys: ServerTrustPolicy.publicKeys(),
        validateCertificateChain: true,
        validateHost: true
    )*/
    :
]

fileprivate let manager: Manager = {

    let configuration = URLSessionConfiguration.default
    configuration.urlCache = nil

    return Manager(
        configuration: URLSessionConfiguration.default/*,
         serverTrustPolicyManager: ServerTrustPolicyManager(policies: policies)*/
    )
}()

fileprivate let networkLoggerPlugin = NetworkLoggerPlugin(verbose: true, responseDataFormatter: JSONResponseDataFormatter)


class WSProvider {

    static var shared: WSProvider = WSProvider()
    
    fileprivate let reachability = NetworkReachabilityManager()
    
    #if DEBUG
    fileprivate static var sharedProvider = MoyaProvider<NetworkAPI>(endpointClosure: endpointClosure, stubClosure: stubClosure, callbackQueue: DispatchQueue.global(qos: .utility) ,manager: manager, plugins: [networkActivityPlugin,networkLoggerPlugin])
    #else
    fileprivate static var sharedProvider = MoyaProvider<NetworkAPI>(endpointClosure: endpointClosure, callbackQueue: DispatchQueue.global(qos: .utility), manager: manager, plugins: [networkActivityPlugin])
    #endif
    
}

extension WSProvider {
    
    @discardableResult
    internal func wsRequest<T>(
        _ target: NetworkAPI,
        success: WSResponse<T>.WSSuccessCompletion?,
        wrappedSuccess: WSResponse<T>.WSWrappedSuccessCompletion?,
        failure: @escaping WSFailureCompletion
    ) -> Cancellable? {
        
        guard target.needAuthorization else {
            return self.performWsRequest(target, success: success, wrappedSuccess: wrappedSuccess, failure: failure)
        }
        
        guard let currentAccount = TokenManager.sharedManager.currentAccount else {
            DispatchQueue.main.async {
                NavigationHelper().showWelcomeScreen()
            }
            return nil
        }
        
        TokenManager.sharedManager.acquireTokenSilently(currentAccount) { [weak self] refreshed in
            guard let self = self else { return }
            
            if refreshed {
                _ = self.performWsRequest(target, success: success, wrappedSuccess: wrappedSuccess, failure: failure)
            } else {
                DispatchQueue.main.async {
                    NavigationHelper().showWelcomeScreen()
                }
            }
        }
        
        return nil
    }
    
    
    fileprivate func performWsRequest<T>(
        _ target: NetworkAPI,
        success: WSResponse<T>.WSSuccessCompletion?,
        wrappedSuccess: WSResponse<T>.WSWrappedSuccessCompletion?,
        failure: @escaping WSFailureCompletion
    ) -> Cancellable? {
        
        let stubBehaviour = WSProvider.sharedProvider.stubClosure(target)
        
        switch stubBehaviour {
        case .never:
            if let reachability = reachability, reachability.isReachable {
                return WSProvider.sharedProvider.request(target) { [unowned self] (result) in
                    self.handleResult(target, result: result, success: success, wrappedSuccess: wrappedSuccess , failure: failure)
                }
            } else {
                NetworkActivityManager.sharedManager.showNotReachablePopup()
                handleError(target, errorType: .networkError(error: nil), failure: failure)
                return nil
            }
        default:
            return WSProvider.sharedProvider.request(target) { [unowned self] (result) in
                self.handleResult(target, result: result, success: success, wrappedSuccess: wrappedSuccess , failure: failure)
            }
        }
    }
    
    fileprivate func handleResult<T>(_ target: NetworkAPI, result: MoyaResult, success: WSResponse<T>.WSSuccessCompletion?, wrappedSuccess: WSResponse<T>.WSWrappedSuccessCompletion?, failure: @escaping WSFailureCompletion) {

        URLCache.shared.removeAllCachedResponses()
        switch result {
        case let .success(moyaResponse):
            
            let rawDictionary = moyaResponse.getRawDictionary()
            let statusCode = moyaResponse.statusCode
            let errorMessage = rawDictionary?[KEY_ERROR_MESSAGE] as? String
            let code = rawDictionary?[KEY_CODE_MESSAGE] as? Int
            
            if statusCode == WSStatusCode.authorizationError.rawValue {
                DispatchQueue.main.async {
                    NavigationHelper().showWelcomeScreen()
                }
            } else if statusCode == WSStatusCode.success.rawValue {
                
                let internalStatusCode = WSStatusCode(rawValue:code ?? 0) ?? .unknown
                if internalStatusCode == .successWithMessage || internalStatusCode == .authorizationError || internalStatusCode == .errorWithMessage {
                    showMessageIfNeccesary(statusCode: internalStatusCode, errorMessage: errorMessage)
                }
                
                if internalStatusCode == .success || internalStatusCode == .successWithMessage || internalStatusCode == .warning {
                    guard let response = T.parse(moyaResponse) else {
                        handleError(target, errorType: .parseError, failure: failure)
                        return
                    }
                    DispatchQueue.main.async {
                        success?(response)
                        wrappedSuccess?(WSResponse(statusCode: internalStatusCode, errorMessage: errorMessage, data: response, rawDictionary:rawDictionary))
                    }
                } else if internalStatusCode == .error || internalStatusCode == .authorizationError || internalStatusCode == .errorWithMessage  {
                    handleError(target, errorType: .internalError(statusCode: internalStatusCode, errorMessage: errorMessage), failure: failure)
                }
                
            } else {
                handleError(target, errorType: .serverError, failure: failure)
            }
            break
        case let .failure(error):
            if (error as NSError).code != NSURLErrorCancelled {
                handleError(target, errorType: .networkError(error:error), failure: failure)
                switch error {
                case .underlying(let error, _):
                    if target.needToShowLoading {
                        showNetworkError(error: error)
                    }
                    break
                default: break
                }
            }
        }
    }
    
    fileprivate func handleError(_ target: NetworkAPI, errorType:WSError, failure: @escaping WSFailureCompletion) {
        DispatchQueue.main.async {
            #if DEBUG
                debugPrint(errorType.errorDescription + " for " + target.path)
            #endif
            failure(errorType)
        }
    }
}

extension WSProvider {
    
    // MARK: Utils
    
    fileprivate func showMessageIfNeccesary(statusCode:WSStatusCode,errorMessage:String?) {
        guard let message = errorMessage else { return }
        DispatchQueue.main.async {
            NavigationHelper().showBasicAlert(with: statusCode.errorTitle, message: message)
        }
    }
    
    @discardableResult
    fileprivate func showNetworkError(error:Error) -> String? {
        let errorMessage = (error as NSError).localizedDescription
        DispatchQueue.main.async {
            NavigationHelper().showBasicAlert(with: WSStatusCode.error.errorTitle, message: errorMessage)
        }
        return errorMessage
    }
    
    @discardableResult
    func wsAuthorizedRequest<T>(
        _ target: NetworkAPI,
        success: WSResponse<T>.WSSuccessCompletion?,
        wrappedSuccess: WSResponse<T>.WSWrappedSuccessCompletion? = nil,
        failure: @escaping WSFailureCompletion
    ) -> Cancellable? {
        
        // only do token check if the target wants authorization
        guard target.needAuthorization else {
            return self.wsRequest(target, success: success, wrappedSuccess: wrappedSuccess, failure: failure)
        }
        
        TokenManager.sharedManager.acquireTokenSilently(TokenManager.sharedManager.currentAccount) { [weak self] refreshed in
            guard let self = self else { return }
            
            if refreshed {
                // token refreshed, now proceed
                _ = self.wsRequest(target, success: success, wrappedSuccess: wrappedSuccess, failure: failure)
            } else {
                // token refresh failed - force sign out or handle
                DispatchQueue.main.async {
                    NavigationHelper().showWelcomeScreen()
                }
            }
        }
        
        return nil
    }

}

private func JSONResponseDataFormatter(_ data: Data) -> Data {
    do {
        let dataAsJSON = try JSONSerialization.jsonObject(with: data)
        let prettyData =  try JSONSerialization.data(withJSONObject: dataAsJSON, options: .prettyPrinted)
        return prettyData
    } catch {
        return data // fallback to original data if it can't be serialized.
    }
}


