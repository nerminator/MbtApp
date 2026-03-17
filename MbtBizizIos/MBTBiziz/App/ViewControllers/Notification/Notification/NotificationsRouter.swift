//
//  NotificationsRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol NotificationsRoutingLogic
{
    func routeToDetail(_ item : MBTNotificationListItem?)
}

protocol NotificationsDataPassing
{
    var dataStore: NotificationsDataStore? { get }
}

class NotificationsRouter: NSObject, NotificationsRoutingLogic, NotificationsDataPassing
{
    weak var viewController: NotificationsViewController?
    var dataStore: NotificationsDataStore?
    
    //MARK : Routing
    func routeToDetail(_ item : MBTNotificationListItem?) {
        let detail = NewsDetailViewController.fromStoryboard(.newsDetail)
        var destination = detail.router?.dataStore
        destination?.listItem = item
        viewController?.navigationController?.pushViewController(detail, animated: true)
    }
    
}
