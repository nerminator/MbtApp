//
//  PageContainerWorker.swift
//
//  Created by Serkut Yegin on 31.01.2018.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class PageContainerWorker
{
    func doSetup(_ viewController : PageContainerViewController) {
        let interactor = PageContainerInteractor()
        let presenter = PageContainerPresenter()
        let router = PageContainerRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
}
