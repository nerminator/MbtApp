//
//  ProfileWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class ProfileWorker
{
    func doSetup(_ viewController : ProfileViewController) {
        let interactor = ProfileInteractor()
        let presenter = ProfilePresenter()
        let router = ProfileRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getProfile(_ completion:@escaping(_ response : MBTProfileResponse?)->()) {
        WSProvider.shared.wsRequest(.getProfile, map: completion)
    }
}
