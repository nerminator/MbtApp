//
//  WorkCalendarRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol WorkCalendarRoutingLogic
{

}

protocol WorkCalendarDataPassing
{
    var dataStore: WorkCalendarDataStore? { get }
}

class WorkCalendarRouter: NSObject, WorkCalendarRoutingLogic, WorkCalendarDataPassing
{
    weak var viewController: WorkCalendarViewController?
    var dataStore: WorkCalendarDataStore?
    
    //MARK : Routing
    
}
