//
//  NewsWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class NewsWorker
{
    func doSetup(_ viewController : NewsViewController) {
        let interactor = NewsInteractor()
        let presenter = NewsPresenter()
        let router = NewsRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    @discardableResult
    func getNewsList(_ newsType : NewsType, discountType : DiscountType?, locId:Int?, pageNumber : Int, completion: @escaping (_ response : MBTNewsResponse?)->()) -> MBTCancellable? {
        return WSProvider.shared.wsRequest(.getNewsList(type: newsType, discountType: discountType, locId: locId, pageNumber: pageNumber), map: completion)
    }
}
