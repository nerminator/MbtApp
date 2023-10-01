//
//  MealMenuModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum MealMenu
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request {
            var additionalInfo : [String : Any]
        }
        
        struct Response {
            var densityInfo : [MBTFoodMenuDensityInfo]?
            var densityHeight : CGFloat
        }
        
        struct ViewModel {
            var densityInfo : [MBTFoodMenuDensityInfo]?
            var densityHeight : CGFloat
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
