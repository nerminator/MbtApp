//
//  LocationSearchWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class LocationSearchWorker
{
    func doSetup(_ viewController : LocationSearchViewController) {
        let interactor = LocationSearchInteractor()
        let presenter = LocationSearchPresenter()
        let router = LocationSearchRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
}
