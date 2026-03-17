//
//  MealMenuWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class MealMenuWorker
{
    func doSetup(_ viewController : MealMenuViewController) {
        let interactor = MealMenuInteractor()
        let presenter = MealMenuPresenter()
        let router = MealMenuRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
}
