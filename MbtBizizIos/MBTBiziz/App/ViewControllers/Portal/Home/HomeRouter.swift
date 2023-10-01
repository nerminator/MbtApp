//
//  HomeRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol HomeRoutingLogic
{
    func routeToPortalNews()
    func routeToPortalLocation()
    func routeToPortalTransportation()
    func routeToPortalMenu(_ mealInfo : MBTFoodMenuResponse)
    func routeToBarcode()
}

protocol HomeDataPassing
{
    var dataStore: HomeDataStore? { get }
}

class HomeRouter: NSObject, HomeRoutingLogic, HomeDataPassing, BarcodeRoutable
{
    weak var viewController: HomeViewController?
    var dataStore: HomeDataStore?
    
    //MARK : Routing
    func routeToPortalNews() {
        viewController?.navigationController?.pushViewController(PageContainerViewController.buildPagerForPortalNews(), animated: true)
    }
    
    func routeToPortalLocation() {
        viewController?.navigationController?.pushViewController(LocationViewController.fromStoryboard(.location), animated: true)
    }
    
    func routeToPortalTransportation() {
        viewController?.navigationController?.pushViewController(TransportationOptionsViewController.fromStoryboard(.transportationOptions), animated: true)
    }
    
    func routeToPortalMenu(_ mealInfo: MBTFoodMenuResponse) {
        viewController?.navigationController?.pushViewController(PageContainerViewController.buildPagerForPortalMenu(mealInfo), animated: true)
    }

    func routeToBarcode() {

        routeToBarcode(from: viewController)
    }
}
