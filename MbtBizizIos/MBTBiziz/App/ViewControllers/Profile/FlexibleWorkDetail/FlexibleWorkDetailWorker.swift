//
//  FlexibleWorkDetailWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 20.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class FlexibleWorkDetailWorker
{
    func doSetup(_ viewController : FlexibleWorkDetailViewController) {
        let interactor = FlexibleWorkDetailInteractor()
        let presenter = FlexibleWorkDetailPresenter()
        let router = FlexibleWorkDetailRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getMonthlyWorkHours(_ year:Int, month:Int,completion:@escaping(_ response : MBTFlexibleMonthlyResponse?)->()) {
        WSProvider.shared.wsRequest(.getMonthlyWorkHours(year: year, month: month), map: completion)
    }
}
