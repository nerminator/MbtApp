//
//  TransportationSearchRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationSearchRoutingLogic
{
    func routeToBack(_ selectedStopListItem : MBTTransportationListStopList?)
}

protocol TransportationSearchDataPassing
{
    var dataStore: TransportationSearchDataStore? { get }
}

class TransportationSearchRouter: NSObject, TransportationSearchRoutingLogic, TransportationSearchDataPassing
{
    weak var viewController: TransportationSearchViewController?
    var dataStore: TransportationSearchDataStore?
    
    //MARK : Routing
    func routeToBack(_ selectedStopListItem: MBTTransportationListStopList?) {
        dataStore?.searchCompletionBlock?(selectedStopListItem)
        viewController?.navigationController?.popViewController(animated: true)
    }
    
}
