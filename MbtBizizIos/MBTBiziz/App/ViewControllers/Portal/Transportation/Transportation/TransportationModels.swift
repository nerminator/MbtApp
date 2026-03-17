//
//  TransportationModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum Transportation
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request {
        }
        
        struct Response {
            var tabItems : [String]
            var shuttleList : [MBTTransportationListShuttleList]
            var arrivalText : String?
        }
        
        struct ViewModel {
            var tabItems : [String]
            var shuttleList : [MBTTransportationListShuttleList]
            var arrivalText : String?
        }
    }
    
    enum Validate {
        struct Request {
            
        }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Reload {
        struct Request {
            var selectedTabIndex : Int
        }
        
        struct Response {
            var shuttleList : [MBTTransportationListShuttleList]
        }
        
        struct ViewModel {
            var shuttleList : [MBTTransportationListShuttleList]
        }
    }
    
    enum SendData {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum SearchResult {
        
        struct Request {
            var selectedSearchItem : MBTTransportationListStopList?
        }
        
        struct Response {
            var shuttleList : [MBTTransportationListShuttleList]
            var arrivalText : String?
            var departureText : String?
        }
        
        struct ViewModel {
            var shuttleList : [MBTTransportationListShuttleList]
            var arrivalText : String?
            var departureText : String?
        }
    }
    
    enum SwitchLocations {
        struct Request {
        }
        
        struct Response {
            var tabItems : [String]
            var shuttleList : [MBTTransportationListShuttleList]
        }
        
        struct ViewModel {
            var tabItems : [String]
            var shuttleList : [MBTTransportationListShuttleList]
        }
    }
    
    enum BecomeFirstResponder {
        
        struct Request {
            var isArrivalField : Bool
        }
        
        struct Response {
            var needToRouteSearch : Bool
            var shuttleList : [MBTTransportationListShuttleList]?
        }
        
        struct ViewModel {
            var needToRouteSearch : Bool
            var shuttleList : [MBTTransportationListShuttleList]?
        }
    }
    
    enum ClearTextField {
        
        struct Request {
            
        }
        
        struct Response {
            var shuttleList : [MBTTransportationListShuttleList]
            var arrivalText : String?
            var departureText : String?
        }
        
        struct ViewModel {
            var shuttleList : [MBTTransportationListShuttleList]
            var arrivalText : String?
            var departureText : String?
        }
    }
    
}
