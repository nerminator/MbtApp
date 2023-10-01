//
//  AboutRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol AboutRoutingLogic
{
    func routeToTermOfUse(_ htmlType: HtmlType)
}

protocol AboutDataPassing
{
    var dataStore: AboutDataStore? { get }
}

class AboutRouter: NSObject, AboutRoutingLogic, AboutDataPassing
{
    weak var viewController: AboutViewController?
    var dataStore: AboutDataStore?
    
    //MARK : Routing
    
    func routeToTermOfUse(_ htmlType: HtmlType) {
        let termsOfUse = TermOfUseViewController.fromStoryboard(.termsOfUse)
        termsOfUse.termsOfUseType = .info
        termsOfUse.htmlType = htmlType
        viewController?.navigationController?.pushViewController(termsOfUse, animated: true)
    }
    
}
