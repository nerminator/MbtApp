//
//  SettingsRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 31.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol SettingsRoutingLogic
{

}

protocol SettingsDataPassing
{
    var dataStore: SettingsDataStore? { get }
}

class SettingsRouter: NSObject, SettingsRoutingLogic, SettingsDataPassing
{
    weak var viewController: SettingsViewController?
    var dataStore: SettingsDataStore?
    
    //MARK : Routing
    
}
