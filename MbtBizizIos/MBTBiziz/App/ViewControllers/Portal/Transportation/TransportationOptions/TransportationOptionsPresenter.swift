//
//  TransportationOptionsPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationOptionsPresentationLogic
{
    func presentInitializeViewResult(response : TransportationOptions.Initialize.Response)
    func presentValidateInputs(response : TransportationOptions.Validate.Response)
    func presentReloadPageResult(response : TransportationOptions.Reload.Response)
    func presentSendDataResult(response : TransportationOptions.SendData.Response)
}

class TransportationOptionsPresenter: TransportationOptionsPresentationLogic
{
    weak var viewController: TransportationOptionsDisplayLogic?
    
    func presentInitializeViewResult(response : TransportationOptions.Initialize.Response) {
        
        let pickerModelType = UIPickerView.commonPickerWithToolbar
        let pickerModelCompanyList = UIPickerView.commonPickerWithToolbar
        
        response.textFieldType.inputView = pickerModelType.pickerView
        response.textFieldType.inputAccessoryView = pickerModelType.toolBar
        response.textFieldCompanyList.inputView = pickerModelCompanyList.pickerView
        response.textFieldCompanyList.inputAccessoryView = pickerModelCompanyList.toolBar
        pickerModelType.pickerView.delegate = viewController
        pickerModelType.pickerView.dataSource = viewController
        pickerModelCompanyList.pickerView.delegate = viewController
        pickerModelCompanyList.pickerView.dataSource = viewController
        pickerModelType.doneButton.target = viewController
        pickerModelCompanyList.doneButton.target = viewController
        
        viewController?.displayInitializeViewResult(viewModel : TransportationOptions.Initialize.ViewModel(pickerModelType: pickerModelType, pickerModelCompanyList: pickerModelCompanyList))
    }
    
    func presentValidateInputs(response : TransportationOptions.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : TransportationOptions.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : TransportationOptions.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : TransportationOptions.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : TransportationOptions.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : TransportationOptions.SendData.ViewModel(companyListItem:response.companyListItem, response: response.response))
    }
}
