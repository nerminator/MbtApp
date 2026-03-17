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
    func routeToSapConcur()
    func routeToOrchestra()
    func routeToImprovement()
    func routeToOracleHcm()
    func routeToSocialMedia()
    func routeToDinner()
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
        UIApplication.shared.open(URL(string:"https://performancemanager5.successfactors.eu/sf/careers/jobsearch?bplte_company=mercedesbe&_s.crb=McXCkAgE%252fh%252bQRsZU9VjG2iZj%252f4QF%252b%252bcAtBlOkPdHY4U%253d")!)
        WSProvider.shared.wsRequest(.menuIncrement(keyName: "InternalAds"))
    }
    
    func routeToBirthday() {
        let birthday = BirthdayListViewController.fromStoryboard(.birthdayList)
        viewController?.navigationController?.pushViewController(birthday, animated: true)
    }
    
    func routeToSapConcur() {
      // test a crash to see that log is sent
      //  let array = NSArray(object: "Test")
      //  let _ = array.object(at: 2) // Index out of bounds → raises NSException
        
       UIApplication.shared.open(URL(string: "https://apps.apple.com/tr/app/sap-concur/id335023774?l=tr")!)
        WSProvider.shared.wsRequest(.menuIncrement(keyName: "SapConcur"))
    }
    
    func routeToDinner() {
        UIApplication.shared.open(URL(string: "https://mercedes-benz.rezervem.com.tr/")!)
        WSProvider.shared.wsRequest(.menuIncrement(keyName: "YemegimEvimde"))
            
    }
    
    func routeToOrchestra() {
        UIApplication.shared.open(URL(string: "https://apps.apple.com/tr/app/cloudoffix/id1524199913?l=tr")!)
        WSProvider.shared.wsRequest(.menuIncrement(keyName: "CloudOffix"))
    }
    
    func routeToImprovement() {
        UIApplication.shared.open(URL(string: "https://daimlertruck.crowdworx.com/ideas")!)
        WSProvider.shared.wsRequest(.menuIncrement(keyName: "Ideas"))
    }
    
    func routeToOracleHcm() {
        UIApplication.shared.open(URL(string: "https://login-exdu-saasfaprod1.fa.ocs.oraclecloud.com/")!)
        WSProvider.shared.wsRequest(.menuIncrement(keyName: "OracleHcm"))
    }
    func routeToSocialMedia() {
        let sm = SocialMediaViewController.fromStoryboard(.socialmedia)
        viewController?.navigationController?.pushViewController(sm, animated: true)
    }
}
