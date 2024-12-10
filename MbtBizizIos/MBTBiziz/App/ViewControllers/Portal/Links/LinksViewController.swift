//
//  HomeViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.10.2023.
//  Copyright © 2023 Daimler AG. All rights reserved.
//

import UIKit

protocol LinksDisplayLogic: class
{
  /*  func displayInitializeViewResult(viewModel : Home.Initialize.ViewModel)
    func displayValidateInputs(viewModel : Home.Validate.ViewModel)
    func displayReloadPageResult(viewModel : Home.Reload.ViewModel)
    func displaySendDataResult(viewModel : Home.SendData.ViewModel)
    
    func displayMealInfoResult(viewModel : Home.MealInfo.ViewModel)*/
}

class LinksViewController: MBTBaseViewController, LinksDisplayLogic
{
    
    // MARK: IBOutlets
    @IBOutlet weak var viewCardClubs: MBTHomeCardView!
    @IBOutlet weak var viewCardAds: MBTHomeCardView!
    @IBOutlet weak var viewCardBirthdays: MBTHomeCardView!
    @IBOutlet weak var viewCardMbFit: MBTHomeCardView!
    @IBOutlet weak var viewCardGursel: MBTHomeCardView!
    @IBOutlet weak var viewCardOrchestra: MBTHomeCardView!
    @IBOutlet weak var viewCardImprovement: MBTHomeCardView!
    @IBOutlet weak var viewCardCar: MBTHomeCardView!
    @IBOutlet weak var viewCardLibrary: MBTHomeCardView!
    
    
    // MARK: VIP Protocols
    
    var interactor: LinksBusinessLogic?
    var router: (NSObjectProtocol & LinksRoutingLogic & LinksDataPassing)?

    // MARK: Object lifecyrequires the types 'MBTLocationItem' and 'MarshalResponse' be equivalentcle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        LinksWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        LinksWorker().doSetup(self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
    }
    
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
    }
}

extension LinksViewController {
    
    //MARK: - Display Logic
 /*   func displayInitializeViewResult(viewModel : Links.Initialize.ViewModel) { }
    
    func displayValidateInputs(viewModel : Links.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : Links.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : Links.SendData.ViewModel) { }
    
    func displayMealInfoResult(viewModel: Links.MealInfo.ViewModel) {
        router?.routeToPortalMenu(viewModel.mealInfo)
    }*/
}

extension LinksViewController : MBTHomeCardViewDelegate {
    
    func homeCardViewDidSelected(_ homeCard: MBTHomeCardView) {
        if homeCard == viewCardClubs {
            router?.routeToClubs()
        } else if homeCard == viewCardAds {
            router?.routeToAds()
        } else if homeCard == viewCardBirthdays {
            router?.routeToBirthday()
        } else if homeCard == viewCardMbFit {
            router?.routeToMbFit()
        } else if homeCard == viewCardGursel {
            router?.routeToGursel()
        } else if homeCard == viewCardOrchestra {
            router?.routeToOrchestra()
        } else if homeCard == viewCardImprovement {
            router?.routeToImprovement()
        } else if homeCard == viewCardCar{
            router?.routeToCar()
        } else if homeCard == viewCardLibrary {
            router?.routeToLibrary()
        }
    }
}
