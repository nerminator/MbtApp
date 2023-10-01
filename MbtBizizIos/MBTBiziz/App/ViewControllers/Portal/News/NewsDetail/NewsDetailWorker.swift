//
//  NewsDetailWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class NewsDetailWorker
{
    func doSetup(_ viewController : NewsDetailViewController) {
        let interactor = NewsDetailInteractor()
        let presenter = NewsDetailPresenter()
        let router = NewsDetailRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getNewsDetail(_ identifier:Int, completion:@escaping (_ response : MBTNewsDetailResponse?)->()) {
        WSProvider.shared.wsRequest(.getNewsDetail(identifier: identifier), map: completion)
    }
}
