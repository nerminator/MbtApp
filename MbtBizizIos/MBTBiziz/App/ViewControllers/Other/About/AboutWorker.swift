//
//  AboutWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class AboutWorker
{
    func doSetup(_ viewController : AboutViewController) {
        let interactor = AboutInteractor()
        let presenter = AboutPresenter()
        let router = AboutRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
}
