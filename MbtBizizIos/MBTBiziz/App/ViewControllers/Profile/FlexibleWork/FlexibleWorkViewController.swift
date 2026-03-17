//
//  FlexibleWorkViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol FlexibleWorkDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : FlexibleWork.Initialize.ViewModel)
    func displayValidateInputs(viewModel : FlexibleWork.Validate.ViewModel)
    func displayReloadPageResult(viewModel : FlexibleWork.Reload.ViewModel)
    func displaySendDataResult(viewModel : FlexibleWork.SendData.ViewModel)
}

class FlexibleWorkViewController: MBTBaseViewController, FlexibleWorkDisplayLogic
{
    
    // MARK: IBOutlets

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var textFieldYear: UITextField!
    @IBOutlet weak var lblYear: BaseUILabelDemi!
    @IBOutlet weak var constYearPickerBottom: NSLayoutConstraint!
    
    fileprivate var pickerItems : [String] = []
    
    // MARK: VIP Protocols
    
    var interactor: FlexibleWorkBusinessLogic?
    var router: (NSObjectProtocol & FlexibleWorkRoutingLogic & FlexibleWorkDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        FlexibleWorkWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        FlexibleWorkWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.title = interactor?.getNavigationTitle()
        constYearPickerBottom.constant = -100
        interactor?.initializeView(request: FlexibleWork.Initialize.Request())
    }
    
    @objc func pickerFinished(_ sender : UIBarButtonItem) {
        textFieldYear.resignFirstResponder()
        if let picker = textFieldYear.inputPicker {
            let item = pickerItems[picker.selectedRow(inComponent: 0)]
            if item != lblYear.text {
                lblYear.text = item
                interactor?.reloadPage(request: FlexibleWork.Reload.Request(selectedYear: item))
            }
        }
    }
}

extension FlexibleWorkViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : FlexibleWork.Initialize.ViewModel) {
        self.pickerItems = viewModel.pickerItems ?? []
        lblYear.text = "\(interactor?.selectedYear ?? Date().year)"
        textFieldYear.inputView = viewModel.pickerModel.pickerView
        textFieldYear.inputAccessoryView = viewModel.pickerModel.toolBar
        viewModel.pickerModel.doneButton.target = self
        viewModel.pickerModel.doneButton.action = #selector(pickerFinished(_:))
        viewModel.pickerModel.pickerView.delegate = self
        viewModel.pickerModel.pickerView.dataSource = self
        viewModel.pickerModel.pickerView.reloadAllComponents()
        tableView.reloadData()
        
        if pickerItems.count > 0 {
            constYearPickerBottom.constant = 24
            view.layoutIfNeededAnimated()
        }
        
    }
    
    func displayValidateInputs(viewModel : FlexibleWork.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : FlexibleWork.Reload.ViewModel) {
        tableView.reloadData()
    }
    
    func displaySendDataResult(viewModel : FlexibleWork.SendData.ViewModel) { }
}

extension FlexibleWorkViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return interactor?.yearlyWorkHours.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: FlexibleTableCell.className, for: indexPath) as! FlexibleTableCell
        cell.setup(interactor?.yearlyWorkHours[indexPath.row], plusKey: interactor?.plusKey, minusKey: interactor?.minusKey, index: indexPath.row)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        guard let year = interactor?.selectedYear, let month = interactor?.yearlyWorkHours[indexPath.row].month else { return }
        router?.routeToDetail(year, month: month, navBarAddition: interactor?.getNavigationSmallTitle())
    }
}

extension FlexibleWorkViewController : UIPickerViewDelegate, UIPickerViewDataSource {
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerItems.count
    }
    
    func pickerView(_ pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
        return NSAttributedString(string: pickerItems[row], attributes: [.foregroundColor: UIColor.white, .font: UIFont.mbtRegular(23)!])
    }
    
    func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat {
        return 35
    }
}
