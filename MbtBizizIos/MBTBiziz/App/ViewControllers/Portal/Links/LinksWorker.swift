//
//  LinksWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class LinksWorker
{
    func doSetup(_ viewController : LinksViewController) {
        let interactor = LinksInteractor()
        let presenter = LinksPresenter()
        let router = LinksRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getMealInfo(_ completion : @escaping (_ response : MBTFoodMenuResponse?)->()) {
        WSProvider.shared.wsRequest(.getFoodMenu, map: completion)
    }

    func fetchUserConfig(_ completion: @escaping (_ shouldShowQrCode: Bool) -> Void) {

        WSProvider.shared.wsRequest(.getUserConfig, success: { (response: MBTUserConfigResponse?) in
            completion(response?.shouldShowQrCode ?? false)
        }) { (error) in
            completion(false)
        }
    }
}
