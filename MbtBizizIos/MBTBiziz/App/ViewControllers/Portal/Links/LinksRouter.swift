//
//  LinksRouter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

    
protocol LinksRoutingLogic
{
    func routeToClubs()
    func routeToAds()
    func routeToBirthday()
    func routeToMbFit()
    func routeToGursel()
    func routeToOrchestra()
    func routeToImprovement()
    func routeToCar()
    func routeToLibrary()
}

protocol LinksDataPassing
{
    //var dataStore: LinksDataStore? { get }
}

class LinksRouter: NSObject, LinksRoutingLogic, LinksDataPassing
{
    weak var viewController: LinksViewController?
    var dataStore: LinksDataStore?
    
    //MARK : Routing
    func routeToClubs() {
        let clubs = ClubLocsViewController.fromStoryboard(.clubsLoc)
        viewController?.navigationController?.pushViewController(clubs, animated: true)
    }
    
    func routeToAds() { //URL
        UIApplication.shared.open(URL(string:"https://performancemanager5.successfactors.eu/acme?bplte_company=mercedesbe&fbacme_n=recruiting&recruiting%5fns=joblisting%20summary&_s.crb=9ZPnbTenOQEkBst64BF17GYPU8usDnQOfv2MFLy3e1U%3d")!)
    }
    
    func routeToBirthday() {
        let birthday = BirthdayListViewController.fromStoryboard(.birthdayList)
        viewController?.navigationController?.pushViewController(birthday, animated: true)
    }
    
    func routeToMbFit() {
       UIApplication.shared.open(URL(string: "https://apps.apple.com/tr/app/wellbees/id1459175907?l=tr")!)
    }
    
    func routeToGursel() {
        UIApplication.shared.open(URL(string: "https://apps.apple.com/app/g%C3%BCrsel-yolcu/id1549465749")!)
            
    }
    
    func routeToOrchestra() {
        UIApplication.shared.open(URL(string: "https://apps.apple.com/tr/app/cloudoffix/id1524199913?l=tr")!)
    }
    
    func routeToImprovement() {
        UIApplication.shared.open(URL(string: "https://daimler-truck.ideas.cloud/app/")!)
    }
    
    func routeToCar() {
        UIApplication.shared.open(URL(string: "https://vts.tr152.corpintra.net/account/LoginPortal?returnUrl=/")!)
    }
    func routeToLibrary() {
        UIApplication.shared.open(URL(string: "https://kitapdunyasi.tr152.corpintra.net/")!)
    }
}
