//
//  FlexibleWorkDetailViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 20.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol FlexibleWorkDetailDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : FlexibleWorkDetail.Initialize.ViewModel)
    func displayValidateInputs(viewModel : FlexibleWorkDetail.Validate.ViewModel)
    func displayReloadPageResult(viewModel : FlexibleWorkDetail.Reload.ViewModel)
    func displaySendDataResult(viewModel : FlexibleWorkDetail.SendData.ViewModel)
}

class FlexibleWorkDetailViewController: MBTBaseViewController, FlexibleWorkDetailDisplayLogic
{
    // MARK: IBOutlets
    @IBOutlet weak var tableView: UITableView!
    
    // MARK: VIP Protocols
    
    var interactor: FlexibleWorkDetailBusinessLogic?
    var router: (NSObjectProtocol & FlexibleWorkDetailRoutingLogic & FlexibleWorkDetailDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        FlexibleWorkDetailWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        FlexibleWorkDetailWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        interactor?.initializeView(request: FlexibleWorkDetail.Initialize.Request())
    }
}

extension FlexibleWorkDetailViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : FlexibleWorkDetail.Initialize.ViewModel) {
        self.navigationItem.title = viewModel.yearMonthStr
    }
    
    func displayValidateInputs(viewModel : FlexibleWorkDetail.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : FlexibleWorkDetail.Reload.ViewModel) {
        tableView.reloadData()
    }
    
    func displaySendDataResult(viewModel : FlexibleWorkDetail.SendData.ViewModel) { }
}

extension FlexibleWorkDetailViewController : UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return interactor?.monthlyWorkHours.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: FlexibleDetailCell.className, for: indexPath) as! FlexibleDetailCell
        cell.setup(indexPath.row, workDetails: interactor?.monthlyWorkHours[indexPath.row], plusKey: interactor?.plusKey, minusKey: interactor?.minusKey)
        return cell
    }
}
