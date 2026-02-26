//
//  LocationWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class LocationWorker
{
    func doSetup(_ viewController : LocationViewController) {
        let interactor = LocationInteractor()
        let presenter = LocationPresenter()
        let router = LocationRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getLocationDetails(_ completion: @escaping (_ response : [MBTLocationItem]?)->()) {
        WSProvider.shared.wsRequest(.getLocations, map: completion)
    }
}
