//
//  ProfileViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol ProfileDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : Profile.Initialize.ViewModel)
    func displayValidateInputs(viewModel : Profile.Validate.ViewModel)
    func displayReloadPageResult(viewModel : Profile.Reload.ViewModel)
    func displaySendDataResult(viewModel : Profile.SendData.ViewModel)
}

class ProfileViewController: MBTBaseViewController, ProfileDisplayLogic
{
    
    // MARK: IBOutlets
    
    @IBOutlet weak var constFlexibleHeight: NSLayoutConstraint!
    @IBOutlet weak var lblFlexible: BaseUILabel!
    @IBOutlet weak var viewCalendar: UIView!
    @IBOutlet weak var viewFlexible: UIView!
    @IBOutlet weak var lblLdap: BaseUILabelDemi!
    @IBOutlet weak var lblUsername: BaseUILabelRegular!
    @IBOutlet weak var lblDuty: BaseUILabel!
    @IBOutlet weak var lblLocation: BaseUILabelRegular!
    @IBOutlet weak var lblOrganization: BaseUILabelRegular!
    
    // MARK: VIP Protocols
    
    var interactor: ProfileBusinessLogic?
    var router: (NSObjectProtocol & ProfileRoutingLogic & ProfileDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        ProfileWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        ProfileWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        interactor?.initializeView(request: Profile.Initialize.Request())
    }
    
    override func viewWillAppear(_ animated: Bool) {
        if !firstWillAppear {
            interactor?.initializeView(request: Profile.Initialize.Request())
        }
        super.viewWillAppear(animated)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        if let destination = segue.destination as? FlexibleWorkViewController {
            var dataStore = destination.router?.dataStore
            dataStore?.navigationTitle = interactor?.yearlyWorkHoursText
            dataStore?.navigationTitleSmall = interactor?.monthlyWorkHoursText
        }
    }
}

extension ProfileViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : Profile.Initialize.ViewModel) {
        lblLdap.text = viewModel.registerNumber
        lblUsername.text = viewModel.nameSurname
        lblDuty.text = viewModel.title
        lblLocation.text = viewModel.officeLocation
        lblOrganization.text = viewModel.organizationUnit
        lblFlexible.text = viewModel.flexibleTitle
        if viewModel.isSuccess {
            viewFlexible.isHidden = false
            viewCalendar.isHidden = false
        }
        constFlexibleHeight.constant = viewModel.flexibleAvailable ? 60 : 0
        view.layoutIfNeeded()
    }
    
    func displayValidateInputs(viewModel : Profile.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : Profile.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : Profile.SendData.ViewModel) { }
}

extension ProfileViewController {
    
    @IBAction func btnFlexibleWorkTapped(_ sender: UIButton) {
    }
    
    @IBAction func btnWorkCalendarTapped(_ sender: UIButton) {
    }
    
    @IBAction func btnPayslipTapped(_ sender: UIButton) {
        // Seçim ekranına yönlendirme yapılacak
        router?.routeToPayslip()
    }
}
