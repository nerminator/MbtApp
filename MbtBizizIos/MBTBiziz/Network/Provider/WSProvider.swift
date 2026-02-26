//
//  WSProvider.swift
//
//  Created by Serkut Yegin.
//

import Moya
import Result
import Alamofire
import Marshal

extension WSProvider {
    
    //MARK: Public Wrappers
    
    /*@discardableResult
    func wsRetry<T>(_ request : WSRequestWrapper<T>) -> Cancellable? {
        return wsRequest(request.target, success: request.success, wrappedSuccess: request.wrappedSuccess, failure: request.failure)
    }*/
    
    @discardableResult
    func wsRequest(_ target: NetworkAPI) -> Cancellable? {
        return wsRequest(target, success: { (_ : WSResponse<MarshalResponse>) in })
    }
    
    @discardableResult
    func wsRequest<T : Parsable>(_ target: NetworkAPI, map: @escaping WSResponse<T>.WSMapCompletion) -> Cancellable? {
        return wsRequest(target, success: { (response : WSResponse<T>) in
            map(response.data)
        }, failure: { (error) in
            map(nil)
        })
    }
    
    @discardableResult
    func wsRequest<T : Parsable>(_ target: NetworkAPI, success: @escaping WSResponse<T>.WSSuccessCompletion) -> Cancellable? {
        return wsRequest(target, success: success, failure: { _ in })
    }
    
    @discardableResult
    func wsRequest<T>(_ target: NetworkAPI, success: @escaping WSResponse<T>.WSWrappedSuccessCompletion) -> Cancellable? {
        return wsRequest(target, success: success, failure: { _ in })
    }
    
    @discardableResult
    func wsRequest<T : Parsable>(_ target: NetworkAPI, success: @escaping WSResponse<T>.WSSuccessCompletion, failure: @escaping WSFailureCompletion) -> Cancellable? {
        return wsRequest(target, success: success, wrappedSuccess: nil, failure: failure)
    }
    
    @discardableResult
    func wsRequest<T>(_ target: NetworkAPI, success: @escaping WSResponse<T>.WSWrappedSuccessCompletion, failure: @escaping WSFailureCompletion) -> Cancellable? {
        return wsRequest(target, success: nil, wrappedSuccess: success, failure: failure)
    }
}

extension WSProvider {
    
    func cancelAllRequests() {
        
        let sessionManager = Alamofire.SessionManager.default
        sessionManager.session.getTasksWithCompletionHandler { dataTasks, uploadTasks, downloadTasks in
            dataTasks.forEach {
                $0.cancel()
            }
            uploadTasks.forEach {
                $0.cancel()
            }
            downloadTasks.forEach {
                $0.cancel()
            }
        }
    }
    
}


