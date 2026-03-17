//
//  WelcomeRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol WelcomeRoutingLogic
{
    func routeToActivation(_ phoneNumber : String)
}

protocol WelcomeDataPassing
{
    var dataStore: WelcomeDataStore? { get }
}

class WelcomeRouter: NSObject, WelcomeRoutingLogic, WelcomeDataPassing
{
    weak var viewController: WelcomeViewController?
    var dataStore: WelcomeDataStore?
    
    //MARK : Routing
    func routeToActivation(_ phoneNumber: String) {
        let activation = ActivationCodeViewController.fromStoryboard(.activationCode)
        var destination = activation.router?.dataStore
        destination?.phoneNumber = phoneNumber
        viewController?.navigationController?.pushViewController(activation, animated: true)
    }
    
}
