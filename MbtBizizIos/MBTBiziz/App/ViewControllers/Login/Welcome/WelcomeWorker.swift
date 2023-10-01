//
//  WelcomeWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class WelcomeWorker
{
    func doSetup(_ viewController : WelcomeViewController) {
        let interactor = WelcomeInteractor()
        let presenter = WelcomePresenter()
        let router = WelcomeRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func checkPhone(_ phoneNumber : String, completion:@escaping(_ isSuccess:Bool)->()) {

        WSProvider.shared.wsRequest(.checkPhone(phoneNumber: phoneNumber), success: { (response : WSResponse<MarshalResponse>) in
            completion(response.isSuccess)
        }) { (error) in
            completion(false)
        }
    }

}
