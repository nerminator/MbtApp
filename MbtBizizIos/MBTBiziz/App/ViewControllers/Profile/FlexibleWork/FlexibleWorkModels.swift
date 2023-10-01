//
//  FlexibleWorkModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum FlexibleWork
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response {
            var selectedYear : Int?
            var years : [Int]?
        }
        
        struct ViewModel {
            var pickerModel : CommonPickerModel
            var pickerItems : [String]?
        }
    }
    
    enum Validate {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Reload {
        struct Request {
            var selectedYear : String
        }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum SendData {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
}
