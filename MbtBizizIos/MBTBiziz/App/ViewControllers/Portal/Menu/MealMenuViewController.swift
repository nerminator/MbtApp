//
//  MealMenuViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol MealMenuDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : MealMenu.Initialize.ViewModel)
    func displayValidateInputs(viewModel : MealMenu.Validate.ViewModel)
    func displayReloadPageResult(viewModel : MealMenu.Reload.ViewModel)
    func displaySendDataResult(viewModel : MealMenu.SendData.ViewModel)
}

class MealMenuViewController: MBTBaseViewController, MealMenuDisplayLogic
{
    
    @IBOutlet weak var tableView: UITableView!
    fileprivate var selectedIndexPath : IndexPath?
    fileprivate var densityHelper : DensityHelper?
    
    // MARK: VIP Protocols
    
    var interactor: MealMenuBusinessLogic?
    var router: (NSObjectProtocol & MealMenuRoutingLogic & MealMenuDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        MealMenuWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        MealMenuWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        interactor?.initializeView(request: MealMenu.Initialize.Request(additionalInfo: self.additionalData))
    }
    
    override func shouldAddGradientLayer() -> Bool {
        return false
    }
}

extension MealMenuViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : MealMenu.Initialize.ViewModel) {
        tableView.reloadData()
        if let densityInfo = viewModel.densityInfo {
            self.densityHelper = DensityHelper()
            self.densityHelper?.showDensityInfoIfNeccesary(self.parent, densityInfo: densityInfo)
        }
        tableView.contentInset.bottom = viewModel.densityHeight
    }
    
    func displayValidateInputs(viewModel : MealMenu.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : MealMenu.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : MealMenu.SendData.ViewModel) { }
}

extension MealMenuViewController : UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 { return 1 }
        else if section == 1 { return interactor?.foodInfo?.foodList?.count ?? 0 }
        else if section == 2 {
            guard let count = interactor?.shuttleInfo?.shuttleList?.count, count > 0 else { return 0 }
            return count + 2
        }
        else { return 0 }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: MealMenuHeaderCell.className, for: indexPath) as! MealMenuHeaderCell
            cell.setup(interactor?.foodInfo?.dateText, isToday: interactor?.foodInfo?.isToday)
            return cell
        } else if indexPath.section == 1 {
            let cell = tableView.dequeueReusableCell(withIdentifier: MealMenuCell.className, for: indexPath) as! MealMenuCell
            cell.setup(interactor?.foodInfo?.foodList?[indexPath.row],index: indexPath.row ,isExpanded: isSelectedIndex(indexPath))
            return cell
        } else if indexPath.section == 2 {
            if indexPath.row == 0 { return tableView.dequeueReusableCell(withIdentifier: MealMenuServiceHeaderCell.className, for: indexPath) }
            else if indexPath.row == (tableView.numberOfRows(inSection: 2) - 1)  { return tableView.dequeueReusableCell(withIdentifier: MealMenuServiceFooterCell.className, for: indexPath)}
            else {
                let cell = tableView.dequeueReusableCell(withIdentifier: MealMenuServiceCell.className, for: indexPath) as! MealMenuServiceCell
                cell.setup(interactor?.shuttleInfo?.shuttleList?[indexPath.row - 1])
                return cell
            }
        }
        return UITableViewCell()
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.section == 0 { return 70 }
        else if indexPath.section == 1 {
            if isSelectedIndex(indexPath) { return UITableViewAutomaticDimension }
            else { return 70 }
        }
        else if indexPath.section == 2 {
            if indexPath.row == 0 { return 70 }
            if indexPath.row == (tableView.numberOfRows(inSection: 2) - 1 ) { return 70 }
            
            guard let item = interactor?.shuttleInfo?.shuttleList?[indexPath.row - 1].timeList else { return 0 }
            return 74 + CGFloat(2 * item.count - 1) * 20
        }
        else { return 0 }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.section == 1 {
            if let previous = selectedIndexPath {
                if isSelectedIndex(indexPath) {
                    (tableView.cellForRow(at: previous) as? MealMenuCell)?.setExpanded(false, animated: true)
                    selectedIndexPath = nil
                } else {
                    (tableView.cellForRow(at: previous) as? MealMenuCell)?.setExpanded(false, animated: true)
                    (tableView.cellForRow(at: indexPath) as? MealMenuCell)?.setExpanded(true, animated: true)
                    selectedIndexPath = indexPath
                }
            } else {
                (tableView.cellForRow(at: indexPath) as? MealMenuCell)?.setExpanded(true, animated: true)
                selectedIndexPath = indexPath
            }
            tableView.beginUpdates()
            tableView.endUpdates()
        }
    }
}

fileprivate extension MealMenuViewController {
    
    func isSelectedIndex(_ indexPath: IndexPath) -> Bool {
        guard let selected = selectedIndexPath else { return false }
        return selected.row == indexPath.row && selected.section == indexPath.section
    }
}
