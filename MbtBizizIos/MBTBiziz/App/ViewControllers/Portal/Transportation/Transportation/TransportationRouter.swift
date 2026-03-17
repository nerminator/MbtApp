//
//  TransportationRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationRoutingLogic
{
    func routeToSearch(_ shuttleList : [MBTTransportationListShuttleList]?, searchCompletion: @escaping SearchCompletionBlock)
}

protocol TransportationDataPassing
{
    var dataStore: TransportationDataStore? { get }
}

class TransportationRouter: NSObject, TransportationRoutingLogic, TransportationDataPassing
{
    weak var viewController: TransportationViewController?
    var dataStore: TransportationDataStore?
    
    //MARK : Routing
    func routeToSearch(_ shuttleList : [MBTTransportationListShuttleList]?, searchCompletion: @escaping SearchCompletionBlock) {
        let searchVC = TransportationSearchViewController.fromStoryboard(.transportationSearch)
        var destination = searchVC.router?.dataStore
        destination?.shuttleList = shuttleList
        destination?.searchCompletionBlock = searchCompletion
        viewController?.navigationController?.pushViewController(searchVC, animated: true)
    }
}
