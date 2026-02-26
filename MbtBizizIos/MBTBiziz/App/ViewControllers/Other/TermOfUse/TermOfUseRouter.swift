//
//  TermOfUseRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TermOfUseRoutingLogic
{
    func routeToTermOfUse(_ htmlType: HtmlType)
}

protocol TermOfUseDataPassing
{
    var dataStore: TermOfUseDataStore? { get }
}

class TermOfUseRouter: NSObject, TermOfUseRoutingLogic, TermOfUseDataPassing
{
    weak var viewController: TermOfUseViewController?
    var dataStore: TermOfUseDataStore?
    
    //MARK : Routing
    func routeToTermOfUse(_ htmlType: HtmlType) {
        let termsOfUse = TermOfUseViewController.fromStoryboard(.termsOfUse)
        termsOfUse.termsOfUseType = .info
        termsOfUse.htmlType = htmlType
        viewController?.navigationController?.pushViewController(termsOfUse, animated: true)
    }
    
}
