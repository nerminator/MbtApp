//
//  LocationResultRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol LocationResultRoutingLogic
{

}

protocol LocationResultDataPassing
{
    var dataStore: LocationResultDataStore? { get }
}

class LocationResultRouter: NSObject, LocationResultRoutingLogic, LocationResultDataPassing
{
    weak var viewController: LocationResultViewController?
    var dataStore: LocationResultDataStore?
    
    //MARK : Routing
    
}
