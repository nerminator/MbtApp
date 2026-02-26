//
//  WorkCalendarModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum WorkCalendar
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Validate {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Reload {
        struct Request {
            var date : Date
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
