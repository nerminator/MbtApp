//
//  BirthdayListViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol BirthdayListDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : BirthdayList.Initialize.ViewModel)
    func displayValidateInputs(viewModel : BirthdayList.Validate.ViewModel)
    func displayReloadPageResult(viewModel : BirthdayList.Reload.ViewModel)
    func displaySendDataResult(viewModel : BirthdayList.SendData.ViewModel)
}

class BirthdayListViewController: MBTBaseViewController, BirthdayListDisplayLogic
{
    // MARK: IBOutlets
    
    @IBOutlet weak var imgViewBg: UIImageView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var constImageTop: NSLayoutConstraint!
    @IBOutlet weak var constImageHeight: NSLayoutConstraint!
    @IBOutlet weak var viewBlur: UIVisualEffectView!
    @IBOutlet weak var viewBlueHeight: NSLayoutConstraint!
    
    fileprivate var topHeight : CGFloat {
        return CGFloat.statusBarHeight + (self.navigationController?.navigationBar.h ?? 0)
    }
    
    // MARK: VIP Protocols
    
    var interactor: BirthdayListBusinessLogic?
    var router: (NSObjectProtocol & BirthdayListRoutingLogic & BirthdayListDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        BirthdayListWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        BirthdayListWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        if #available(iOS 11.0, *) {
            tableView.contentInsetAdjustmentBehavior = .never
        }
        interactor?.sendData(request: BirthdayList.SendData.Request())
    }
}

extension BirthdayListViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : BirthdayList.Initialize.ViewModel) { }
    
    func displayValidateInputs(viewModel : BirthdayList.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : BirthdayList.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : BirthdayList.SendData.ViewModel) {
        tableView.reloadData()
    }
}

extension BirthdayListViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return (interactor?.birthdayList.count ?? 0) + 2
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            return tableView.dequeueReusableCell(withIdentifier: "EmptyCell", for: indexPath)
        } else if indexPath.row == 1 {
            let cell = tableView.dequeueReusableCell(withIdentifier: NewsDetailDateCell.className, for: indexPath) as! NewsDetailDateCell
            cell.lblDate.text = interactor?.dateInfo
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: BirthdayTableCell.className, for: indexPath) as! BirthdayTableCell
            cell.setup(interactor?.birthdayList[indexPath.row - 2], index: indexPath.row - 2)
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 { return NewsDetailConstants.IMAGE_HEIGHT }
        else if indexPath.row == 1 { return 70 }
        else { return 72 }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        handleScroll(for: scrollView.contentOffset.y + scrollView.contentInset.top)
    }
    
    func handleScroll(for offset:CGFloat) {
        handleBackgroundImage(for: offset)
        handleImageViewDepth(for: offset)
    }
    
    func handleBackgroundImage(for offset:CGFloat) {
        if offset <= 0 {
            constImageTop.constant = 0
            constImageHeight.constant = NewsDetailConstants.IMAGE_HEIGHT - offset/2
        } else if offset > 0 && offset < imgViewBg.h - topHeight {
            constImageTop.constant = -offset
            constImageHeight.constant = NewsDetailConstants.IMAGE_HEIGHT
        } else {
            constImageTop.constant = -imgViewBg.h + topHeight
            constImageHeight.constant = NewsDetailConstants.IMAGE_HEIGHT
        }
    }
    
    func handleImageViewDepth(for offset:CGFloat) {
        if offset < imgViewBg.h - topHeight {
            if view.subviews.index(of: imgViewBg) != 0 {
                view.insertSubview(imgViewBg, belowSubview: tableView)
                viewBlur.isHidden = true
            }
        } else {
            if view.subviews.index(of: imgViewBg) == 0 {
                view.insertSubview(imgViewBg, aboveSubview: tableView)
                viewBlur.isHidden = false
                viewBlueHeight.constant = topHeight
                view.layoutIfNeeded()
            }
        }
    }
}
