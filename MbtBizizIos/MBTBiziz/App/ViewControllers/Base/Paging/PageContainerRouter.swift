//
//  PageContainerRouter.swift
//
//  Created by Serkut Yegin on 31.01.2018.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol PageContainerRoutingLogic
{

}

protocol PageContainerDataPassing
{
    var dataStore: PageContainerDataStore? { get }
}

class PageContainerRouter: NSObject, PageContainerRoutingLogic, PageContainerDataPassing
{
    weak var viewController: PageContainerViewController?
    var dataStore: PageContainerDataStore?
    
    //MARK : Routing
    
}
