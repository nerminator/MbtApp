//
//  TransportationWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class TransportationWorker
{
    func doSetup(_ viewController : TransportationViewController) {
        let interactor = TransportationInteractor()
        let presenter = TransportationPresenter()
        let router = TransportationRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
}
