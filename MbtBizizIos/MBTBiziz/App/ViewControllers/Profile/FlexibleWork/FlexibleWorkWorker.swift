//
//  FlexibleWorkWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class FlexibleWorkWorker
{
    func doSetup(_ viewController : FlexibleWorkViewController) {
        let interactor = FlexibleWorkInteractor()
        let presenter = FlexibleWorkPresenter()
        let router = FlexibleWorkRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getYearlyWorkHours(_ selectedYear : Int?, completion:@escaping(_ response: MBTFlexibleYearlyResponse?)->()) {
        WSProvider.shared.wsRequest(.getYearlyWorkHours(year: selectedYear ?? 0), map: completion)
    }
}
