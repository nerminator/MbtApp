//
//  BirthdayListRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol BirthdayListRoutingLogic
{

}

protocol BirthdayListDataPassing
{
    var dataStore: BirthdayListDataStore? { get }
}

class BirthdayListRouter: NSObject, BirthdayListRoutingLogic, BirthdayListDataPassing
{
    weak var viewController: BirthdayListViewController?
    var dataStore: BirthdayListDataStore?
    
    //MARK : Routing
    
}
