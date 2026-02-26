//
//  ActivationCodeModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum ActivationCode
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Validate {
        struct Request {
            var otp:String
        }
        
        struct Response {
            var otp:String
        }
        
        struct ViewModel {
            var isValid : Bool
        }
    }
    
    enum Reload {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum SendData {
        struct Request {
            var otp:Int
        }
        
        struct Response {
            var isSuccess : Bool
            var isExpired: Bool
        }
        
        struct ViewModel {
            var isSuccess : Bool
            var isExpired: Bool
        }
    }
    
    enum Timer {
        struct Request {
            
        }
        
        struct Response {
            var timeRemaining : Int
        }
        
        struct ViewModel {
            var timeRemainingStr : String
            var isResendButtonEnabled : Bool
        }
    }
    
}
