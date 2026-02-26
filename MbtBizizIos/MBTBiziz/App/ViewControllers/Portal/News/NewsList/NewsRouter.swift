//
//  NewsRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol NewsRoutingLogic
{
    func routeToDetail(_ item : MBTNewsNewsList?)
    func routeToBirthday()
}

protocol NewsDataPassing
{
    var dataStore: NewsDataStore? { get }
}

class NewsRouter: NSObject, NewsRoutingLogic, NewsDataPassing
{
    weak var viewController: NewsViewController?
    var dataStore: NewsDataStore?
    
    //MARK : Routing
    func routeToDetail(_ item : MBTNewsNewsList?) {
        let detail = NewsDetailViewController.fromStoryboard(.newsDetail)
        var destination = detail.router?.dataStore
        destination?.listItem = item
        viewController?.navigationController?.pushViewController(detail, animated: true)
    }
    
    func routeToBirthday() {
        let birthday = BirthdayListViewController.fromStoryboard(.birthdayList)
        viewController?.navigationController?.pushViewController(birthday, animated: true)
    }
    
}
