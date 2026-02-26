//
//  LocationRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LocationRoutingLogic
{
    func routeToLocationSearch(_ locations : [MBTLocationItem]?)
}

protocol LocationDataPassing
{
    var dataStore: LocationDataStore? { get }
}

class LocationRouter: NSObject, LocationRoutingLogic, LocationDataPassing
{
    weak var viewController: LocationViewController?
    var dataStore: LocationDataStore?
    
    //MARK : Routing
    func routeToLocationSearch(_ locations: [MBTLocationItem]?) {
        guard let locations = locations else { return }
        let locationSearch = LocationSearchViewController.fromStoryboard(.locationSearch)
        var destination = locationSearch.router?.dataStore
        destination?.arrLocations = locations
        viewController?.navigationController?.pushViewController(locationSearch, animated: true)
    }
    
}
