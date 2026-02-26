//
//  TransportationSearchWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class TransportationSearchWorker
{
    func doSetup(_ viewController : TransportationSearchViewController) {
        let interactor = TransportationSearchInteractor()
        let presenter = TransportationSearchPresenter()
        let router = TransportationSearchRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getPreviousSearchItems() -> [MBTTransportationSearchItem] {
        return MBTTransportationSearchItem.load()
    }
}
