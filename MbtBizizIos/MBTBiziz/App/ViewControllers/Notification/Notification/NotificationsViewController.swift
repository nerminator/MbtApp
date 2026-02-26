//
//  NotificationsViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol NotificationsDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : Notifications.Initialize.ViewModel)
    func displayValidateInputs(viewModel : Notifications.Validate.ViewModel)
    func displayUpdatePageResult(viewModel : Notifications.Update.ViewModel)
    func displayReloadPageResult(viewModel : Notifications.Reload.ViewModel)
    func displaySendDataResult(viewModel : Notifications.SendData.ViewModel)
}

class NotificationsViewController: MBTBaseViewController, NotificationsDisplayLogic
{
    
    @IBOutlet weak var tableView: UITableView!
    // MARK: VIP Protocols
    
    var interactor: NotificationsBusinessLogic?
    var router: (NSObjectProtocol & NotificationsRoutingLogic & NotificationsDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        NotificationsWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        NotificationsWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.tableFooterView = UIView()
        interactor?.initializeView(request: Notifications.Initialize.Request())
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        interactor?.resetNotifications()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        tableView.reloadData()
        if !firstDidAppear {
            interactor?.updatePage(request: Notifications.Update.Request())
        }
        MBTNotificationManager.shared.unreadedNotificationCount = 0
    }
}

extension NotificationsViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : Notifications.Initialize.ViewModel) {
        if isAppeared {
            tableView.reloadData()
        }
    }
    
    func displayValidateInputs(viewModel : Notifications.Validate.ViewModel) { }
    
    func displayUpdatePageResult(viewModel: Notifications.Update.ViewModel) {
        if isAppeared && viewModel.numberOfNewNotifications > 0 {
            var indexPaths = [IndexPath]()
            for index in 0..<viewModel.numberOfNewNotifications {
                indexPaths.append(IndexPath(row: index, section: 0))
            }
            tableView.beginUpdates()
            tableView.insertRows(at: indexPaths, with: .automatic)
            tableView.endUpdates()
        }
    }
    
    func displayReloadPageResult(viewModel : Notifications.Reload.ViewModel) {
        if isAppeared {
            tableView.reloadData()
        }
    }
    
    func displaySendDataResult(viewModel : Notifications.SendData.ViewModel) {
        if isAppeared {
            tableView.beginUpdates()
            tableView.deleteRows(at: [IndexPath(row: viewModel.index, section: 0)], with: .automatic)
            tableView.endUpdates()
        } else {
            tableView.reloadData()
        }
    }
}

extension NotificationsViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return interactor?.arrNotifications.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: NotificationTableCell.className, for: indexPath) as! NotificationTableCell
        cell.setup(interactor?.arrNotifications[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        guard let notifCount = interactor?.arrNotifications.count else { return }
        if indexPath.row == notifCount - 3 {
            interactor?.reloadPage(request: Notifications.Reload.Request())
        }
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return true
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            tableView.setEditing(false, animated: true)
            interactor?.sendData(request: Notifications.SendData.Request(indexPath: indexPath))
        }
    }
    
    func tableView(_ tableView: UITableView, titleForDeleteConfirmationButtonForRowAt indexPath: IndexPath) -> String? {
        return "SİL"
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        router?.routeToDetail(interactor?.arrNotifications[indexPath.row])
    }
    
}
