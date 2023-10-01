//
//  NewsModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum News
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request {
            var additionalData : [String:Any]
        }
        
        struct Response {
            var newsType : NewsType
        }
        
        struct ViewModel {
            var newsType : NewsType
            var pickerModel : CommonPickerModel?
        }
    }
    
    enum Validate {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Reload {
        struct Request { }
        
        struct Response {
            var birthdayCount : Int?
        }
        
        struct ViewModel {
            var birthdayCount : Int?
        }
    }
    
    enum SendData {
        struct Request {
            var indexPath : IndexPath
        }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
}
