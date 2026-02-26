//
//  LocationModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum Location
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response {
            var selectedBuilding : MBTLocationBuildingList?
            var initialSegmentIndex : Int
        }
        
        struct ViewModel {
            var selectedBuilding : MBTLocationBuildingList?
            var initialSegmentIndex : Int
        }
    }
    
    enum Validate {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Reload {
        struct Request {
            var selectedSegment : Int
        }
        
        struct Response {
            var selectedBuilding : MBTLocationBuildingList?
        }
        
        struct ViewModel {
            var selectedBuilding : MBTLocationBuildingList?
        }
    }
    
    enum SendData {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
}
