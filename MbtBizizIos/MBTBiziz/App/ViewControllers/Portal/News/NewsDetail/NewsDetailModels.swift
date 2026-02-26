//
//  NewsDetailModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum NewsDetail
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response {
            var title : String?
        }
        
        struct ViewModel {
            var title : String?
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

    enum GetUserDiscountCode {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
}
