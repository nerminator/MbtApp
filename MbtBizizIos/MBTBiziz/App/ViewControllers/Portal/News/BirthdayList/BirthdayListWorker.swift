//
//  BirthdayListWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class BirthdayListWorker
{
    func doSetup(_ viewController : BirthdayListViewController) {
        let interactor = BirthdayListInteractor()
        let presenter = BirthdayListPresenter()
        let router = BirthdayListRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getBirthdayList(_ completion:@escaping (_ response : MBTBirthdayResponse?)->()) {
        WSProvider.shared.wsRequest(.getBirthdayList, map: completion)
    }
}
