//
//  TransportationOptionsWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class TransportationOptionsWorker
{
    func doSetup(_ viewController : TransportationOptionsViewController) {
        let interactor = TransportationOptionsInteractor()
        let presenter = TransportationOptionsPresenter()
        let router = TransportationOptionsRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getShuttleOptions(_ completion:@escaping (_ response : MBTTransportationOptionsResponse?)->()) {
        WSProvider.shared.wsRequest(.getShuttleOptions, map: completion)
    }
    
    func getShuttleList(_ typeId:Int, companyId:Int, completion:@escaping (_ response : MBTTransportationListResponse?)->()) {
        WSProvider.shared.wsRequest(.getShuttleList(type: typeId, companyLocationId: companyId), map: completion)
    }
}
