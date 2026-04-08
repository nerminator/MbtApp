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
    func displayBusinessCardOptions(shortUrl: String?)
    func displayKVKKDialog()
}

class ProfileViewController: MBTBaseViewController, ProfileDisplayLogic
{
    
    // MARK: IBOutlets
    
    @IBOutlet weak var constFlexibleHeight: NSLayoutConstraint!
    @IBOutlet weak var constBordroHeight: NSLayoutConstraint!
    
    @IBOutlet weak var viewFlexible: UIView!
    @IBOutlet weak var viewBordro: UIView!
    
    @IBOutlet weak var viewCalendar: UIView!
    @IBOutlet weak var lblFlexible: BaseUILabel!

    @IBOutlet weak var lblLdap: BaseUILabelDemi!
    @IBOutlet weak var lblUsername: BaseUILabelRegular!
    @IBOutlet weak var lblRole: BaseUILabel!
    
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
  
    static var isBordroApp: Bool {
        return (Bundle.main.object(forInfoDictionaryKey: "CFBundleExecutable") as? String)?.contains("Bordro") == true
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        if !ProfileViewController.isBordroApp {
            viewBordro.isHidden = true
            constBordroHeight.isActive = false
            viewBordro.heightAnchor.constraint(equalToConstant: 0).isActive = true
        }

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
    @IBAction func btnBusinessCardTapped(_ sender: Any) {
        
        WSProvider.shared.wsRequest(.getUserBusinessCardState) { (_ response: MBTGetBusinessCardStateResponse?) in
            
            if let isActivated = response?.isActivated, isActivated == true, let digitalCardUrl = response?.digitalCardUrl {
                self.displayBusinessCardOptions(shortUrl: digitalCardUrl)
            } else {
                self.displayKVKKDialog()
            }
            
        } failure: { error in
            self.showAlert(message: "Bağlantı Hatası!")
            
        }
    }

    @IBAction func btnPayslipTapped(_ sender: Any) {
        WSProvider.shared.wsRequest(.payslipIsActive, success: { [weak self] (_ response: WSResponse<MarshalResponse>) in
            self?.router?.routeToPayslip()
        }, failure: { [weak self] error in
            guard let self = self else { return }

            switch error {
            case .internalError(_, let errorMessage):
                let message = errorMessage?.trimmingCharacters(in: .whitespacesAndNewlines)
                if let message = message, !message.isEmpty {
                    self.showAlert(message: message)
                } else {
                    self.showAlert(message: "Bağlantı Hatası!")
                }
            default:
                self.showAlert(message: "Bağlantı Hatası!")
            }
        })
    }
}

extension ProfileViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : Profile.Initialize.ViewModel) {
        lblLdap.text = viewModel.registerNumber
        lblUsername.text = viewModel.nameSurname
        lblRole.text = viewModel.title
        lblLocation.text = viewModel.officeLocation
        lblOrganization.text = viewModel.organizationUnit
        lblFlexible.text = viewModel.flexibleTitle
        
        viewFlexible.isHidden =  !viewModel.flexibleAvailable
        
        view.layoutIfNeeded()
    }
    
    func displayValidateInputs(viewModel : Profile.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : Profile.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : Profile.SendData.ViewModel) { }
}

extension ProfileViewController {

    func displayKVKKDialog() {
        self.navigationController?.pushViewController(KvkkViewController.fromStoryboard(.kvkkVc), animated: true)
    }

    func displayBusinessCardOptions(shortUrl: String?) {
        guard let shortUrl = shortUrl, let url = URL(string: shortUrl) else { return }

        
        let alert = UIAlertController(title: "TXT_CARD_MENU_TITLE".localized(), message: nil, preferredStyle: .actionSheet)
        alert.addAction(UIAlertAction(title: "TXT_CARD_MENU_SHOW".localized(), style: .default) { _ in
            UIApplication.shared.open(url)
        })
        
        alert.addAction(UIAlertAction(title: "TXT_CARD_MENU_SHARE".localized(), style: .default) { _ in
                self.shareBusinessCard(link: shortUrl)
            })
        
        //  deactivate business card
        alert.addAction(UIAlertAction(title: "TXT_CARD_MENU_CANCEL".localized(), style: .destructive) { _ in
            self.confirmDeactivateBusinessCard()
        })
        
        alert.addAction(UIAlertAction(title: "TXT_COMMON_CANCEL2".localized(), style: .cancel))
        present(alert, animated: true)
    }

    private func shareBusinessCard(link: String) {
        let items: [Any] = ["TXT_CARD_SHARE_PREFIX".localized(), URL(string: link)!]

        let activityVC = UIActivityViewController(activityItems: items, applicationActivities: nil)

        // iPad destek (crash olmaması için)
        if let popover = activityVC.popoverPresentationController {
            popover.sourceView = self.view
            popover.sourceRect = CGRect(x: self.view.bounds.midX,
                                        y: self.view.bounds.midY,
                                        width: 0, height: 0)
            popover.permittedArrowDirections = []
        }

        self.present(activityVC, animated: true)
    }
    
    
    private func confirmDeactivateBusinessCard() {
        let confirmAlert = UIAlertController(
            title: "TXT_CARD_MENU_CANCEL".localized(),
            message: "TXT_CARD_DEACTIVATE_MESSAGE".localized(),
            preferredStyle: .alert
        )

        confirmAlert.addAction(UIAlertAction(title: "TXT_COMMON_CANCEL".localized(), style: .cancel))
        confirmAlert.addAction(UIAlertAction(title: "TXT_COMMON_YES".localized(), style: .destructive) { _ in
            self.deactivateBusinessCard()
        })

        present(confirmAlert, animated: true)
    }

    private func deactivateBusinessCard() {
        WSProvider.shared.wsRequest(.deactivateDigitalCard) { (_ response: MarshalResponse?) in

            self.showToast("TXT_CARD_DEACTIVATE_TOAST".localized())
            
        } failure: { error in
            self.showToast("Bağlantı hatası!")
        }
    }
    
}

