//
//  MealMenuRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol MealMenuRoutingLogic
{

}

protocol MealMenuDataPassing
{
    var dataStore: MealMenuDataStore? { get }
}

class MealMenuRouter: NSObject, MealMenuRoutingLogic, MealMenuDataPassing
{
    weak var viewController: MealMenuViewController?
    var dataStore: MealMenuDataStore?
    
    //MARK : Routing
    
}
