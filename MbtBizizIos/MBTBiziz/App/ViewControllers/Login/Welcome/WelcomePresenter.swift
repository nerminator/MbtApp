//
//  WelcomePresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol WelcomePresentationLogic
{
    func presentInitializeViewResult(response : Welcome.Initialize.Response)
    func presentValidateInputs(response : Welcome.Validate.Response)
    func presentReloadPageResult(response : Welcome.Reload.Response)
    func presentSendDataResult(response : Welcome.SendData.Response)
}

class WelcomePresenter: WelcomePresentationLogic
{
    weak var viewController: WelcomeDisplayLogic?
    
    func presentInitializeViewResult(response : Welcome.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : Welcome.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : Welcome.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : Welcome.Validate.ViewModel(isValid: response.phoneNumber.length == 10))
    }
    
    func presentReloadPageResult(response : Welcome.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : Welcome.Reload.ViewModel(capthaImage: response.capthaImage))
    }
    
    func presentSendDataResult(response : Welcome.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : Welcome.SendData.ViewModel(isSuccess: response.isSuccess))
    }
}
