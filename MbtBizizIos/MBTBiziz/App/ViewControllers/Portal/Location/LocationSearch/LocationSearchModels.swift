//
//  LocationSearchModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum LocationSearch
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Validate {
        struct Request {
            var index : Int
        }
        
        struct Response {
            var building : MBTLocationBuildingList
            var room : MBTLocationMeetingRoomList
        }
        
        struct ViewModel {
            var building : MBTLocationBuildingList
            var room : MBTLocationMeetingRoomList
        }
    }
    
    enum Reload {
        struct Request {
            var searchText : String
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
