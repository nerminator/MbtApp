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
    
    @IBOutlet weak var viewCardNews: MBTHomeCardView!
    @IBOutlet weak var viewCardLocation: MBTHomeCardView!
    @IBOutlet weak var viewCardTransportation: MBTHomeCardView!
    @IBOutlet weak var viewCardMenu: MBTHomeCardView!

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

    @objc func barButtonQRCodeTapped(_ sender: UIBarButtonItem) {
        router?.routeToBarcode()
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()

        //HomeWorker().fetchUserConfig { [weak self] (shouldShowQr) in
        //    self?.shouldShowQrCode = shouldShowQr
        //    self?.updateBarButtonItems()
        //}
    }

    private func updateBarButtonItems() {

        var items = [UIBarButtonItem(image: #imageLiteral(resourceName: "btnCallcenter"), style: .plain, target: self, action: #selector(barButtonCallCenterTapped(_:)))]
        if shouldShowQrCode {
            items.append(UIBarButtonItem(image: #imageLiteral(resourceName: "iconQr.pdf"), style: .plain, target: self, action: #selector(barButtonQRCodeTapped(_:))))
        }
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
        if homeCard == viewCardNews {
            router?.routeToPortalNews()
        } else if homeCard == viewCardMenu {
            interactor?.getMealInfo(request: Home.MealInfo.Request())
        } else if homeCard == viewCardLocation {
            router?.routeToPortalLocation()
        } else if homeCard == viewCardTransportation {
            router?.routeToPortalTransportation()
        }
    }
}
