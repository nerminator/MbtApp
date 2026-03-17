//
//  TermOfUseWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class TermOfUseWorker
{
    func doSetup(_ viewController : TermOfUseViewController) {
        let interactor = TermOfUseInteractor()
        let presenter = TermOfUsePresenter()
        let router = TermOfUseRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
}
