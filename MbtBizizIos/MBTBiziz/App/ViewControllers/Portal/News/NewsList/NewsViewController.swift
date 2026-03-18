//
//  NewsViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol NewsDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : News.Initialize.ViewModel)
    func displayValidateInputs(viewModel : News.Validate.ViewModel)
    func displayReloadPageResult(viewModel : News.Reload.ViewModel)
    func displaySendDataResult(viewModel : News.SendData.ViewModel)
}

class NewsViewController: MBTBaseViewController, NewsDisplayLogic
{
    
    // MARK: IBOutlets
    
    @IBOutlet weak var viewBirthday: UIView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var viewDiscountType: UIView!
    @IBOutlet weak var constBirthdayHeight: NSLayoutConstraint!
    @IBOutlet weak var lblBirthdayCount: BaseUILabelRegular!
    @IBOutlet weak var lblDiscount: BaseUILabelDemi!
    @IBOutlet weak var textFieldDiscount: UITextField!
    
    fileprivate var lastIndexPath : IndexPath?
    fileprivate var pickerItems = DiscountType.discountList
    
    // MARK: VIP Protocols
    
    var interactor: NewsBusinessLogic?
    var router: (NSObjectProtocol & NewsRoutingLogic & NewsDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        NewsWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        NewsWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.estimatedRowHeight = 100
        interactor?.initializeView(request: News.Initialize.Request(additionalData: additionalData))
    }
    
    override func shouldAddGradientLayer() -> Bool {
        return false
    }
    
    override func shouldAddRefreshControl() -> UITableView? {
        return tableView
    }
    
    override func refreshTableViewTrigerred(_ sender: AnyObject) {
        interactor?.reloadPage(request: News.Reload.Request())
    }
    
}

extension NewsViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : News.Initialize.ViewModel) {
        if let pickerModel = viewModel.pickerModel {
            textFieldDiscount.inputView = pickerModel.pickerView
            textFieldDiscount.inputAccessoryView = pickerModel.toolBar
            pickerModel.doneButton.target = self
            pickerModel.doneButton.action = #selector(pickerFinished(_:))
            pickerModel.pickerView.delegate = self
            pickerModel.pickerView.dataSource = self
            pickerModel.pickerView.reloadAllComponents()
        }
        constBirthdayHeight.constant = viewModel.newsType == .hepsi ? 98 : 0
        viewDiscountType.isHidden = viewModel.newsType != .indirim
        lblDiscount.text = interactor?.discountType.title
        view.layoutIfNeeded()
        interactor?.reloadPage(request: News.Reload.Request())
    }
    
    func displayValidateInputs(viewModel : News.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : News.Reload.ViewModel) {
        lblBirthdayCount.text = viewModel.birthdayCount?.toString
        tableView.reloadData()
        endRefreshing()
    }
    
    func displaySendDataResult(viewModel : News.SendData.ViewModel) {
        tableView.reloadData()
    }
}

extension NewsViewController {
    
    @IBAction func btnBirthdayTapped(_ sender: UIButton) {
        router?.routeToBirthday()
    }
    
    @objc func pickerFinished(_ sender : UIBarButtonItem) {
        textFieldDiscount.resignFirstResponder()
        if let picker = textFieldDiscount.inputPicker {
            interactor?.discountType = pickerItems[picker.selectedRow(inComponent: 0)]
            interactor?.reloadPage(request: News.Reload.Request())
            lblDiscount.text = interactor?.discountType.title
        }
    }
}

extension NewsViewController : UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return interactor?.headers.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if let list = interactor?.items[section] {
            return list.count + 1
        }
        return 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: NewsHeaderCell.className, for: indexPath) as! NewsHeaderCell
            cell.lblHeader.text = interactor?.headers[indexPath.section]
            return cell
        } else {
            guard let newsType = interactor?.newsType else { return UITableViewCell() }
            if newsType == .hepsi {
                let cell = tableView.dequeueReusableCell(withIdentifier: NewsTitleCell.className, for: indexPath) as! NewsTitleCell
                cell.setup(interactor?.items[indexPath.section][indexPath.row - 1])
                return cell
            } else if newsType == .linkler  {
                let cell = tableView.dequeueReusableCell(withIdentifier: NewsLinkCell.className, for: indexPath) as! NewsLinkCell
                cell.setup(interactor?.items[indexPath.section][indexPath.row - 1])
                return cell
            } else if newsType == .baglantilar  {
                let cell = tableView.dequeueReusableCell(withIdentifier: NewsContactCell.className, for: indexPath) as! NewsContactCell
                cell.setup(interactor?.items[indexPath.section][indexPath.row - 1])
                return cell
            } else {
                let cell = tableView.dequeueReusableCell(withIdentifier: NewsSubtitleCell.className, for: indexPath) as! NewsSubtitleCell
                cell.setup(interactor?.items[indexPath.section][indexPath.row - 1])
                return cell
            }
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 { return 46 }
        else {
            guard let newsType = interactor?.newsType else { return 0 }
            if newsType == .indirim { return 120 }
            else { return UITableViewAutomaticDimension }
        }
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        guard let refreshControl = refreshControl, !refreshControl.isRefreshing else { return }
        let lastSection = numberOfSections(in: tableView) - 1
        let lastRow = self.tableView(tableView, numberOfRowsInSection: lastSection) - 1
        if indexPath.section == lastSection && indexPath.row == lastRow {
            if lastIndexPath == nil || !(indexPath.section == lastIndexPath!.section && indexPath.row == lastIndexPath!.row) {
                lastIndexPath = indexPath
                interactor?.sendData(request: News.SendData.Request(indexPath: indexPath))
            }
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.row != 0 {
            let item = interactor?.items[indexPath.section][indexPath.row - 1]
            if (item?.typeEnum == NewsType.linkler ){
                if(item?.url != nil){
                    guard let url = URL(string: item!.url! ) else { return }
                    UIApplication.shared.open(url)
                }
            } else if (item?.typeEnum == NewsType.baglantilar ){
                if(item?.phone != nil){
                    guard let url = URL(string: "tel://" + item!.phone! ) else { return }
                    if( UIApplication.shared.canOpenURL(url)){
                        UIApplication.shared.open(url)
                    }
                }
            }
            else {
                router?.routeToDetail(item)
            }
        }
    }
}

extension NewsViewController : UIPickerViewDelegate, UIPickerViewDataSource {
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerItems.count
    }
    
    func pickerView(_ pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
        return NSAttributedString(string: pickerItems[row].title, attributes: [.foregroundColor: UIColor.white, .font: UIFont.mbtRegular(23)!])
    }
    
    func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat {
        return 35
    }
}
