//
//  WelcomeModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum Welcome
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Validate {
        struct Request {
            var phoneNumber : String
        }
        
        struct Response {
            var phoneNumber : String
        }
        
        struct ViewModel {
            var isValid : Bool
        }
    }
    
    enum Reload {
        struct Request { }
        
        struct Response {

            var capthaImage: String?
        }
        
        struct ViewModel {

            var capthaImage: String?
        }
    }
    
    enum SendData {
        struct Request {
            var phoneNumber : String
        }
        
        struct Response {
            var isSuccess : Bool
        }
        
        struct ViewModel {
            var isSuccess : Bool
        }
    }
    
}
