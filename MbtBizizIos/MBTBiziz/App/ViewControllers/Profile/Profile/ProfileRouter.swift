//
//  ProfileRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol ProfileRoutingLogic
{

}

protocol ProfileDataPassing
{
    var dataStore: ProfileDataStore? { get }
}

class ProfileRouter: NSObject, ProfileRoutingLogic, ProfileDataPassing
{
    weak var viewController: ProfileViewController?
    var dataStore: ProfileDataStore?
    
    //MARK : Routing
    
}
