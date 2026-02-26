//
//  FlexibleWorkDetailRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 20.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol FlexibleWorkDetailRoutingLogic
{

}

protocol FlexibleWorkDetailDataPassing
{
    var dataStore: FlexibleWorkDetailDataStore? { get }
}

class FlexibleWorkDetailRouter: NSObject, FlexibleWorkDetailRoutingLogic, FlexibleWorkDetailDataPassing
{
    weak var viewController: FlexibleWorkDetailViewController?
    var dataStore: FlexibleWorkDetailDataStore?
    
    //MARK : Routing
    
}
