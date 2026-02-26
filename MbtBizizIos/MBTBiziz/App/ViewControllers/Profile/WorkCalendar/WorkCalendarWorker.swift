//
//  WorkCalendarWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class WorkCalendarWorker
{
    func doSetup(_ viewController : WorkCalendarViewController) {
        let interactor = WorkCalendarInteractor()
        let presenter = WorkCalendarPresenter()
        let router = WorkCalendarRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    @discardableResult
    func getWorkCalendar(_ date:String, completion:@escaping(_ response : MBTWorkCalendarResponse?)->()) -> MBTCancellable? {
        return WSProvider.shared.wsRequest(.getWorkCalendar(date: date), map: completion)
    }
}
