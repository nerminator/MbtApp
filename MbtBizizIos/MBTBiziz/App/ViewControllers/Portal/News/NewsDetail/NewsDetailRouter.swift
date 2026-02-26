//
//  NewsDetailRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol NewsDetailRoutingLogic
{

}

protocol NewsDetailDataPassing
{
    var dataStore: NewsDetailDataStore? { get }
}

class NewsDetailRouter: NSObject, NewsDetailRoutingLogic, NewsDetailDataPassing
{
    weak var viewController: NewsDetailViewController?
    var dataStore: NewsDetailDataStore?
    
    //MARK : Routing
    
}
