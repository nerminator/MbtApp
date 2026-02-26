//
//  FlexibleWorkRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol FlexibleWorkRoutingLogic
{
    func routeToDetail(_ year:Int, month:Int, navBarAddition: String?)
}

protocol FlexibleWorkDataPassing
{
    var dataStore: FlexibleWorkDataStore? { get }
}

class FlexibleWorkRouter: NSObject, FlexibleWorkRoutingLogic, FlexibleWorkDataPassing
{
    weak var viewController: FlexibleWorkViewController?
    var dataStore: FlexibleWorkDataStore?
    
    //MARK : Routing
    
    func routeToDetail(_ year: Int, month: Int, navBarAddition: String?) {
        let detail = FlexibleWorkDetailViewController.fromStoryboard(.flexibleWorkDetail)
        var destination = detail.router?.dataStore
        destination?.selectedYear = year
        destination?.selectedMonth = month
        destination?.navBarTitleAddition = navBarAddition
        viewController?.navigationController?.pushViewController(detail, animated: true)
    }
}
