//
//  PageContainerViewController+Builder.swift
//
//  Created by Serkut Yegin on 31.01.2018.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

extension UIViewController {
    func withNavigation() -> UINavigationController {
        let navCon = BaseNavCon(rootViewController: self)
        return navCon
    }
}

extension PageContainerViewController {
    
    class func buildPagerForPortalNews(_ newsType: NewsType, locId: Int? = nil ) -> PageContainerViewController {
        let pager = PageContainerViewController.fromStoryboard(.pageContainer)
        var destination = pager.router?.dataStore
        pager.headers = false
        var pageItems = [PageItem]()
        

        pageItems.append(PageItem(title: newsType.title,
                                      storyboardIdentifier: .news,
                                      additionalData:[
                                        MBTConstants.UserDefined.NewsTypeKey : newsType,
                                        MBTConstants.UserDefined.LocIdKey : locId,
                                      ]))
        
        destination?.pageItems = pageItems
        destination?.title = newsType.title
        destination?.tabDistubition = MBTConstants.Device.isIpad ? .equal(spacing: 0) : .proportionally
        return pager
    }
    
    class func buildPagerForPortalMenu(_ mealInfo: MBTFoodMenuResponse) -> PageContainerViewController {
        let pager = PageContainerViewController.fromStoryboard(.pageContainer)
        var destination = pager.router?.dataStore
        var pageItems = [PageItem]()
        var startIndex = 0
        mealInfo.foodInfo?.forEachEnumerated({ (index, foodInfo) in
            var currentDay = false
            if let isToday = foodInfo.isToday, isToday {
                startIndex = index
                currentDay = true
            }
            pageItems.append(PageItem(title: foodInfo.dateTitle ?? "",
                                      storyboardIdentifier: .mealMenu,
                                      additionalData:[MBTConstants.UserDefined.FoodListKey: foodInfo,
                                                      MBTConstants.UserDefined.ShuttleListKey: mealInfo.shuttleInfo as Any,
                                                      MBTConstants.UserDefined.DensityListKey: currentDay ? (mealInfo.densityInfo as Any) : nil,
                                                      MBTConstants.UserDefined.DensityBottomInset : mealInfo.densityInfo == nil ? 0 : DensityHelper.densityViewHeight]))
        })
        
        destination?.startPage = startIndex
        destination?.pageItems = pageItems
        destination?.title = "TXT_MENU_MENU_TITLE".localized()
        destination?.tabDistubition = .equal(spacing: 0)
        return pager
    }
}
