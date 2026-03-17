//
//  SettingsViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 31.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import UserNotifications

protocol SettingsDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : Settings.Initialize.ViewModel)
    func displayValidateInputs(viewModel : Settings.Validate.ViewModel)
    func displayReloadPageResult(viewModel : Settings.Reload.ViewModel)
    func displaySendDataResult(viewModel : Settings.SendData.ViewModel)
}

class SettingsViewController: MBTBaseViewController, SettingsDisplayLogic
{
    
    @IBOutlet weak var constInfoHeight: NSLayoutConstraint!
    @IBOutlet weak var tableView: UITableView!
    // MARK: VIP Protocols
    
    var interactor: SettingsBusinessLogic?
    var router: (NSObjectProtocol & SettingsRoutingLogic & SettingsDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        SettingsWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        SettingsWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    @IBAction func btnDeviceSettingsTapped(_ sender: UIButton) {
        if let url = URL.init(string: UIApplicationOpenSettingsURLString) {
            UIApplication.shared.open(url, options: [:], completionHandler: nil)
        }
    }
    
    @IBAction func btnQuitTapped(_ sender: UIButton) {
        let controller = UIAlertController.init(title: "TXT_COMMON_WARNING".localized(), message: "TXT_PROFILE_SETTINGS_QUIT_WARNING".localized(), preferredStyle: .alert)
        controller.addAction(UIAlertAction.init(title: "TXT_COMMON_YES".localized(), style: .default, handler: { (action) in
            self.interactor?.reloadPage(parentVC: self, request: Settings.Reload.Request())
        }))
        controller.addAction(UIAlertAction.init(title: "TXT_COMMON_NO".localized(), style: .cancel, handler: nil))
        present(controller, animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.tableFooterView = UIView()
        interactor?.initializeView(request: Settings.Initialize.Request())
        
        let notificationCenter = NotificationCenter.default
        notificationCenter.addObserver(self, selector: #selector(appMovedToForeground), name: Notification.Name.UIApplicationWillEnterForeground, object: nil)
    }
    
    @objc func appMovedToForeground() {
        handleInfoViewAccordingToAuthorizationStatus()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        handleInfoViewAccordingToAuthorizationStatus()
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
}

extension SettingsViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : Settings.Initialize.ViewModel) {
        tableView.reloadData()
    }
    
    func displayValidateInputs(viewModel : Settings.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : Settings.Reload.ViewModel) {
        NavigationHelper().showWelcomeScreen()
    }
    
    func displaySendDataResult(viewModel : Settings.SendData.ViewModel) {
    }
}

extension SettingsViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return interactor?.settingList.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SettingsTableCell.className, for: indexPath) as! SettingsTableCell
        cell.setup(with: interactor?.settingList[indexPath.row], index: indexPath.row)
        cell.delegate = self
        return cell
    }
}

extension SettingsViewController : SettingsTableCellDelegate {
    
    func settingCell(_ settingCell: SettingsTableCell, switchValueChangedTo selected: Bool, for index: Int?) {
        interactor?.sendData(request: Settings.SendData.Request(index: index, isOn: selected))
    }
}

fileprivate extension SettingsViewController {
    
    func handleInfoViewAccordingToAuthorizationStatus() {
        isNotificationAuthorizationGranted { [weak self] (granted) in
            DispatchQueue.main.async {
                self?.constInfoHeight.priority = granted ? UILayoutPriority(rawValue: 999) : UILayoutPriority(rawValue: 200)
                self?.tableView.isUserInteractionEnabled = granted
                self?.tableView.alpha = granted ? 1 : 0.4
                self?.view.layoutIfNeeded()
            }
        }
    }
    
    func isNotificationAuthorizationGranted(_ completion: @escaping (_ granted : Bool)->() ) {
        
        UNUserNotificationCenter.current().getNotificationSettings { (settings) in
            if #available(iOS 12.0, *) {
                completion(settings.authorizationStatus == .provisional || settings.authorizationStatus == .authorized)
            } else {
                completion(settings.authorizationStatus == .authorized)
            }
        }
        
    }
}
