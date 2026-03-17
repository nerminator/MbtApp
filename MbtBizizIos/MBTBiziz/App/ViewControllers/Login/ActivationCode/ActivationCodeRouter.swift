//
//  ActivationCodeRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol ActivationCodeRoutingLogic
{
    
}

protocol ActivationCodeDataPassing
{
    var dataStore: ActivationCodeDataStore? { get }
}

class ActivationCodeRouter: NSObject, ActivationCodeRoutingLogic, ActivationCodeDataPassing
{
    weak var viewController: ActivationCodeViewController?
    var dataStore: ActivationCodeDataStore?
    
    //MARK : Routing
    
}
