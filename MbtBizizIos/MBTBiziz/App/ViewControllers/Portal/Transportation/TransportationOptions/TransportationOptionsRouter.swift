//
//  TransportationOptionsRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationOptionsRoutingLogic
{
    func routeToTransportation(_ response: MBTTransportationListResponse, companyListItem : MBTTransportationOptionsCompanyLocationList)
}

protocol TransportationOptionsDataPassing
{
    var dataStore: TransportationOptionsDataStore? { get }
}

class TransportationOptionsRouter: NSObject, TransportationOptionsRoutingLogic, TransportationOptionsDataPassing
{
    weak var viewController: TransportationOptionsViewController?
    var dataStore: TransportationOptionsDataStore?
    
    //MARK : Routing
    func routeToTransportation(_ response: MBTTransportationListResponse, companyListItem : MBTTransportationOptionsCompanyLocationList) {
        let transportation = TransportationViewController.fromStoryboard(.transportation)
        var destination = transportation.router?.dataStore
        destination?.transportationList = response
        destination?.selectedCompanyListItem = companyListItem
        viewController?.navigationController?.pushViewController(transportation, animated: true)
    }
}
