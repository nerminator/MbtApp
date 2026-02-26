//
//  FlexibleWorkPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol FlexibleWorkPresentationLogic
{
    func presentInitializeViewResult(response : FlexibleWork.Initialize.Response)
    func presentValidateInputs(response : FlexibleWork.Validate.Response)
    func presentReloadPageResult(response : FlexibleWork.Reload.Response)
    func presentSendDataResult(response : FlexibleWork.SendData.Response)
}

class FlexibleWorkPresenter: FlexibleWorkPresentationLogic
{
    weak var viewController: FlexibleWorkDisplayLogic?
    
    func presentInitializeViewResult(response : FlexibleWork.Initialize.Response) {
        let years = response.years?.reversed().map({ (year) -> String in
            return "\(year)"
        })
        viewController?.displayInitializeViewResult(viewModel : FlexibleWork.Initialize.ViewModel(pickerModel: UIPickerView.commonPickerWithToolbar, pickerItems: years))
    }
    
    func presentValidateInputs(response : FlexibleWork.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : FlexibleWork.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : FlexibleWork.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : FlexibleWork.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : FlexibleWork.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : FlexibleWork.SendData.ViewModel())
    }
}
