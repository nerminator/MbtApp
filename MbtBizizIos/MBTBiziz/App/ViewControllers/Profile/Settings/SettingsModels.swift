//
//  SettingsModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 31.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum Settings
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
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum SendData {
        
        struct Request {
            var index: Int?
            var isOn : Bool
        }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
}
