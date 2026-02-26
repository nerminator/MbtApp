//
//  ProfileModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum Profile
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response {
            var profileResponse : MBTProfileResponse?
        }
        
        struct ViewModel {
            var nameSurname : String?
            var flexibleTitle : String?
            var title : String?
            var officeLocation : String?
            var organizationUnit : String?
            var manager : String?
            var registerNumber : String?
            var flexibleAvailable : Bool
            var isSuccess : Bool
        }
    }
    
    enum Validate {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Reload {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum SendData {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
}
