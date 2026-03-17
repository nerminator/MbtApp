//
//  NotificationsModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum Notifications
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
    
    enum Update {
        struct Request { }
        
        struct Response {
            var numberOfNewNotifications : Int
        }
        
        struct ViewModel {
            var numberOfNewNotifications : Int
        }
    }
    
    enum Reload {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum SendData {
        struct Request {
            var indexPath : IndexPath
        }
        
        struct Response {
            var index : Int
        }
        
        struct ViewModel {
            var index : Int
        }
    }
    
}
