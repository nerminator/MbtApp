//
//  WSCodes.swift
//
//  Created by Serkut Yegin.
//

import UIKit

public let KEY_ERROR_MESSAGE = "errorMessage"
public let KEY_CODE_MESSAGE = "statusCode"
public let KEY_PAYLOAD_MESSAGE = "responseData"

enum WSInternalErrorCode : Int {
    case captchaError = 10017
    case showCaptcha = 10018
    case redirectToActivation = 10019
}

enum WSError {
    
    case networkError(error:Error?)
    case serverError
    case parseError
    case internalError(statusCode:WSStatusCode, errorMessage:String?)
    
    var errorDescription : String {
        switch self {
        case .networkError: return "Network Error Occured"
        case .serverError: return "Server Error Occured"
        case .parseError: return "Parse Error Occured"
        case .internalError(_,_): return "Internal Error Occured"
        }
    }
    
    var isInternal : Bool {
        switch self {
        case .internalError(_, _):
            return true
        default:
            return false
        }
    }
}

enum WSStatusCode : Int {
    
    case success = 200
    case successWithMessage = 201
    case warning = 300
    case error = 400
    case authorizationError = 401
    case errorWithMessage = 402
    case preConditionError = 428
    case kickUser = 500
    case unknown = 0
    
    var hasError : Bool {
        if self == .error || self == .authorizationError || self == .preConditionError || self == .kickUser {
            return true
        }
        return false
    }
    
    var isSuccess : Bool {
        return self == .success || self == .successWithMessage
    }
    
    var errorTitle : String {
        switch self {
        case .successWithMessage: return "TXT_COMMON_INFO".localized()
        case .warning,.preConditionError: return "TXT_COMMON_WARNING".localized()
        case .authorizationError, .kickUser: return "TXT_COMMON_ERROR".localized()
        default: return "TXT_COMMON_ERROR".localized()
        }
    }
}


