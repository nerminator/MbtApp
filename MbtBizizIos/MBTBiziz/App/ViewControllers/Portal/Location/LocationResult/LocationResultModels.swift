//
//  LocationResultModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum LocationResult
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response {
            var selectedBuilding : MBTLocationBuildingList?
            var selectedRoomIndex : Int?
        }
        
        struct ViewModel {
            var selectedBuilding : MBTLocationBuildingList?
            var selectedRoomIndex : Int?
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
