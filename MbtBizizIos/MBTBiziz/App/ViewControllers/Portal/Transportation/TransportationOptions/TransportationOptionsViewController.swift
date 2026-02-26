//
//  TransportationOptionsViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationOptionsDisplayLogic: class, UIPickerViewDataSource, UIPickerViewDelegate
{
    func displayInitializeViewResult(viewModel : TransportationOptions.Initialize.ViewModel)
    func displayValidateInputs(viewModel : TransportationOptions.Validate.ViewModel)
    func displayReloadPageResult(viewModel : TransportationOptions.Reload.ViewModel)
    func displaySendDataResult(viewModel : TransportationOptions.SendData.ViewModel)
}

class TransportationOptionsViewController: MBTBaseViewController, TransportationOptionsDisplayLogic
{
    
    // MARK: IBActions
    
    @IBOutlet weak var constButtonBottom: NSLayoutConstraint!
    @IBOutlet weak var textFieldType: BaseUITextField!
    @IBOutlet weak var textFieldCompanyLocation: BaseUITextField!
    @IBOutlet weak var btnContinue: BaseUIButtonDemi!
    
    // MARK: VIP Protocols
    
    var interactor: TransportationOptionsBusinessLogic?
    var router: (NSObjectProtocol & TransportationOptionsRoutingLogic & TransportationOptionsDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        TransportationOptionsWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        TransportationOptionsWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        textFieldType.attributedPlaceholder = NSAttributedString.init(string: "TXT_SHUTTLE_SHUTTLE_TYPE_PLACEHOLDER".localized(),
                                                                        attributes: [.foregroundColor : UIColor.white.withAlphaComponent(0.5),
                                                                                     .font: UIFont.mbtRegular(18)!])
        textFieldCompanyLocation.attributedPlaceholder = NSAttributedString.init(string: "TXT_SHUTTLE_SHUTTLE_COMPANY_LOCATION_PLACEHOLDER".localized(),
                                                                        attributes: [.foregroundColor : UIColor.white.withAlphaComponent(0.5),
                                                                                     .font: UIFont.mbtRegular(18)!])
        interactor?.initializeView(request: TransportationOptions.Initialize.Request(textFieldType: textFieldType, textFieldCompanyList: textFieldCompanyLocation))
    }
    
    override func shouldListenKeyboardNotification() -> Bool {
        return true
    }
    
    override func keyboardAnimation(willShow: Bool, endFrame: CGRect) {
        constButtonBottom.constant = willShow ? endFrame.height - UIView.safeAreaBottomInset + 20 : 20
    }
}

extension TransportationOptionsViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : TransportationOptions.Initialize.ViewModel) {
        viewModel.pickerModelType.doneButton.action = #selector(pickerTypeFinished(_:))
        viewModel.pickerModelCompanyList.doneButton.action = #selector(pickerCompanyListFinished(_:))
        textFieldType.inputPicker?.reloadAllComponents()
        textFieldCompanyLocation.inputPicker?.reloadAllComponents()
    }
    
    func displayValidateInputs(viewModel : TransportationOptions.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : TransportationOptions.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : TransportationOptions.SendData.ViewModel) {
        router?.routeToTransportation(viewModel.response, companyListItem: viewModel.companyListItem)
    }
}

extension TransportationOptionsViewController {
    
    @objc func pickerTypeFinished(_ sender : UIBarButtonItem) {
        guard let pickerView = textFieldType.inputPicker else { return }
        textFieldType.text = interactor?.typeList[pickerView.selectedRow(inComponent: 0)].name
        enableButtonIfNeccesary()
        textFieldType.resignFirstResponder()
    }
    
    @objc func pickerCompanyListFinished(_ sender : UIBarButtonItem) {
        guard let pickerView = textFieldCompanyLocation.inputPicker else { return }
        textFieldCompanyLocation.text = interactor?.companyLocationList[pickerView.selectedRow(inComponent: 0)].name
        enableButtonIfNeccesary()
        if textFieldType.text == nil || textFieldType.text!.isEmpty {
            textFieldType.becomeFirstResponder()
        } else {
            textFieldCompanyLocation.resignFirstResponder()
        }
    }
    
    fileprivate func enableButtonIfNeccesary() {
        btnContinue.isEnabled = textFieldType.text != nil && !textFieldType.text!.isEmpty && textFieldCompanyLocation.text != nil && !textFieldCompanyLocation.text!.isEmpty
    }
    
    @IBAction func btnContinueTapped(_ sender: UIButton) {
        guard let selectedRowType = textFieldType.inputPicker?.selectedRow(inComponent: 0), let selectedRowCompanyList = textFieldCompanyLocation.inputPicker?.selectedRow(inComponent: 0) else { return }
        interactor?.sendData(request: TransportationOptions.SendData.Request(selectedRowType: selectedRowType, selectedRowCompanyList: selectedRowCompanyList))
    }
}

extension TransportationOptionsViewController : UITextFieldDelegate {
    
    func textFieldDidBeginEditing(_ textField: UITextField) {
        textField.superview?.borderColor = UIColor.mbtBlue
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {
        textField.superview?.borderColor = UIColor.clear
    }
}

extension TransportationOptionsViewController {
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if pickerView == textFieldType.inputPicker { return interactor?.typeList.count ?? 0 }
        else { return interactor?.companyLocationList.count ?? 0 }
    }
    
    func pickerView(_ pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
        if pickerView == textFieldType.inputPicker { return NSAttributedString(string: interactor?.typeList[row].name ?? "", attributes: [.foregroundColor: UIColor.white, .font: UIFont.mbtRegular(23)!]) }
        else { return NSAttributedString(string: interactor?.companyLocationList[row].name ?? "", attributes: [.foregroundColor: UIColor.white, .font: UIFont.mbtRegular(23)!]) }
    }
    
    func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat {
        return 35
    }
    
}
