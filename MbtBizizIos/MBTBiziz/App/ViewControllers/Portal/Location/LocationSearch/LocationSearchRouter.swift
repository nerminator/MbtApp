//
//  LocationSearchRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LocationSearchRoutingLogic
{
    func routeToLocationResult(_ building: MBTLocationBuildingList?, room:MBTLocationMeetingRoomList?)
}

protocol LocationSearchDataPassing
{
    var dataStore: LocationSearchDataStore? { get }
}

class LocationSearchRouter: NSObject, LocationSearchRoutingLogic, LocationSearchDataPassing
{
    weak var viewController: LocationSearchViewController?
    var dataStore: LocationSearchDataStore?
    
    //MARK : Routing
    func routeToLocationResult(_ building: MBTLocationBuildingList?, room: MBTLocationMeetingRoomList?) {
        let resultVC = LocationResultViewController.fromStoryboard(.locationResult)
        var destination = resultVC.router?.dataStore
        destination?.selectedBuilding = building
        destination?.selectedRoom = room
        viewController?.navigationController?.pushViewController(resultVC, animated: true)
    }
    
}
