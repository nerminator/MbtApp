//
//  PageContainerInteractor.swift
//
//  Created by Serkut Yegin on 31.01.2018.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

protocol PageContainerBusinessLogic
{
    func buildPageItems()
    var buttonTitles : [String] { get set }
    var viewControllers : [UIViewController] { get set }
}

protocol PageContainerDataStore
{
    var pageItems : [PageItem] { get set }
    var title : String? { get set }
    var startPage : Int { get set }
    var tabDistubition : TabDistrubition { get set }
}

class PageContainerInteractor: PageContainerBusinessLogic, PageContainerDataStore
{
    var presenter: PageContainerPresentationLogic?
    var worker: PageContainerWorker?
    
    var pageItems : [PageItem] = []
    var buttonTitles : [String] = []
    var viewControllers : [UIViewController] = []
    var title: String?
    var startPage: Int = 0
    var tabDistubition: TabDistrubition = .proportionally
    
    func buildPageItems() {
        var tempButtonTitles = [String]()
        var tempControllers = [UIViewController]()
        pageItems.forEach { (item) in
            tempButtonTitles.append(item.title)
            let controller = MBTBaseViewController.fromStoryboard(item.storyboardIdentifier)
            controller.additionalData = item.additionalData ?? [:]
            tempControllers.append(controller)
        }
        self.buttonTitles = tempButtonTitles
        self.viewControllers = tempControllers
        self.presenter?.presentReloadView(response: PageContainer.Initial.Response(startPage: startPage, distrubition: tabDistubition))
    }
  
}
