//
//  LocationResultWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class LocationResultWorker
{
    func doSetup(_ viewController : LocationResultViewController) {
        let interactor = LocationResultInteractor()
        let presenter = LocationResultPresenter()
        let router = LocationResultRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
}
