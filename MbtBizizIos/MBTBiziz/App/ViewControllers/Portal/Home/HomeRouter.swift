//
//  HomeRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol HomeRoutingLogic
{
    func routeToAboutUs()
    func routeToPortalNews(newsType: NewsType)
    func routeToPortalLocation()
    func routeToPortalTransportation()
    func routeToPortalMenu(_ mealInfo : MBTFoodMenuResponse)
    func routeToPortalLinks()
    func routeToPortalPhones() 
}

protocol HomeDataPassing
{
    var dataStore: HomeDataStore? { get }
}

class HomeRouter: NSObject, HomeRoutingLogic, HomeDataPassing
{
    weak var viewController: HomeViewController?
    var dataStore: HomeDataStore?
    
    //MARK : Routing
    func routeToAboutUs() {
        viewController?.navigationController?.pushViewController(AboutUsViewController.fromStoryboard(.aboutUs), animated: true)
    }
    
    func routeToPortalNews(newsType: NewsType) {
	        viewController?.navigationController?.pushViewController(PageContainerViewController.buildPagerForPortalNews(newsType), animated: true)
    }
    
    func routeToPortalLocation() {
        viewController?.navigationController?.pushViewController(LocationViewController.fromStoryboard(.location), animated: true)
    }
    
    func routeToPortalTransportation() {
        UIApplication.shared.open(URL(string: "https://apps.apple.com/app/g%C3%BCrsel-yolcu/id1549465749")!)
            
    }
    
    func routeToPortalMenu(_ mealInfo: MBTFoodMenuResponse) {
        viewController?.navigationController?.pushViewController(PageContainerViewController.buildPagerForPortalMenu(mealInfo), animated: true)
    }

    func routeToPortalLinks() {
        viewController?.navigationController?.pushViewController(LinksViewController.fromStoryboard(.links), animated: true)
    }
    func routeToPortalPhones() {
        let phones = PhoneLocsViewController.fromStoryboard(.phonesLoc)
        viewController?.navigationController?.pushViewController(phones, animated: true)
    }
}
