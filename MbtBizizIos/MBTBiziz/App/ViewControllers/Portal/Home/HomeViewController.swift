//
//  HomeViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol HomeDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : Home.Initialize.ViewModel)
    func displayValidateInputs(viewModel : Home.Validate.ViewModel)
    func displayReloadPageResult(viewModel : Home.Reload.ViewModel)
    func displaySendDataResult(viewModel : Home.SendData.ViewModel)
    
    func displayMealInfoResult(viewModel : Home.MealInfo.ViewModel)
}

class HomeViewController: MBTBaseViewController, HomeDisplayLogic
{
    
    // MARK: IBOutlets
    
    @IBOutlet weak var viewCardAbout: MBTHomeCardView!
    @IBOutlet weak var viewCardNews: MBTHomeCardView!
    @IBOutlet weak var viewCardEvents: MBTHomeCardView!
    @IBOutlet weak var viewCardLocation: MBTHomeCardView!
    @IBOutlet weak var viewCardTransportation: MBTHomeCardView!
    @IBOutlet weak var viewCardMenu: MBTHomeCardView!
    @IBOutlet weak var viewCardDiscounts: MBTHomeCardView!
    @IBOutlet weak var viewCardLinks: MBTHomeCardView!
    @IBOutlet weak var viewCardPhones: MBTHomeCardView!
    
    private var shouldShowQrCode: Bool = false
    
    // MARK: VIP Protocols
    
    var interactor: HomeBusinessLogic?
    var router: (NSObjectProtocol & HomeRoutingLogic & HomeDataPassing)?

    // MARK: Object lifecyrequires the types 'MBTLocationItem' and 'MarshalResponse' be equivalentcle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        HomeWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        HomeWorker().doSetup(self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        updateBarButtonItems()
    }
    
    @objc func barButtonCallCenterTapped(_ sender: UIBarButtonItem) {
        self.tabBarController?.presentAsBottomPopup(.technicalSupport)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        
        //HomeWorker().fetchUserConfig { [weak self] (shouldShowQr) in
        //    self?.shouldShowQrCode = shouldShowQr
        //    self?.updateBarButtonItems()
        //}
    }

    private func updateBarButtonItems() {

        var items = [UIBarButtonItem(image: #imageLiteral(resourceName: "btnCallcenter"), style: .plain, target: self, action: #selector(barButtonCallCenterTapped(_:)))]
        self.navigationItem.leftBarButtonItems = items
    }
}

extension HomeViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : Home.Initialize.ViewModel) { }
    
    func displayValidateInputs(viewModel : Home.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : Home.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : Home.SendData.ViewModel) { }
    
    func displayMealInfoResult(viewModel: Home.MealInfo.ViewModel) {
        router?.routeToPortalMenu(viewModel.mealInfo)
    }
}

extension HomeViewController : MBTHomeCardViewDelegate {
    
    func homeCardViewDidSelected(_ homeCard: MBTHomeCardView) {
        if homeCard == viewCardAbout {
            router?.routeToAboutUs()
        } else if homeCard == viewCardNews {
            router?.routeToPortalNews(newsType: NewsType.duyurular)
        } else if homeCard == viewCardEvents {
            router?.routeToPortalNews(newsType: NewsType.etkinlik)
        } else if homeCard == viewCardLocation {
            router?.routeToPortalLocation()
        } else if homeCard == viewCardTransportation {
            router?.routeToPortalTransportation()
        } else if homeCard == viewCardMenu {
            interactor?.getMealInfo(request: Home.MealInfo.Request())
        } else if homeCard == viewCardDiscounts {
            router?.routeToPortalNews(newsType: NewsType.indirim)
        } else if homeCard == viewCardLinks{
            router?.routeToPortalLinks()
        } else if homeCard == viewCardPhones {
            router?.routeToPortalPhones()
        }
    }
}
