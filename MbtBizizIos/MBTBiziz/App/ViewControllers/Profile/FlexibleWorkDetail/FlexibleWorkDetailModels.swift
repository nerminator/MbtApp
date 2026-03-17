//
//  FlexibleWorkDetailModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 20.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum FlexibleWorkDetail
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response {
            var year : Int
            var month : Int
            var navbarTitleAddition : String?
        }
        
        struct ViewModel {
            var yearMonthStr : String
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
