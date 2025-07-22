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
    func routeToPayslip()
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
    
    func routeToPayslip() {
            let storyboard = UIStoryboard(name: "Payslip", bundle: nil)
            if let vc = storyboard.instantiateViewController(withIdentifier: "PayslipViewController") as? PayslipViewController {
                viewController?.navigationController?.pushViewController(vc, animated: true)
            }
        }
    
}
