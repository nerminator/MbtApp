//
//  HomeModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum Home
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
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum MealInfo {
        struct Request { }
        
        struct Response {
            var mealInfo : MBTFoodMenuResponse
        }
        
        struct ViewModel {
            var mealInfo : MBTFoodMenuResponse
        }
    }
    
}
