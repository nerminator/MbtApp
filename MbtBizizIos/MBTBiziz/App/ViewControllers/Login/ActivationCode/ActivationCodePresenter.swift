//
//  ActivationCodePresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol ActivationCodePresentationLogic
{
    func presentInitializeViewResult(response : ActivationCode.Initialize.Response)
    func presentValidateInputs(response : ActivationCode.Validate.Response)
    func presentReloadPageResult(response : ActivationCode.Reload.Response)
    func presentSendDataResult(response : ActivationCode.SendData.Response)
    
    func presentRemainingTimeResult(response : ActivationCode.Timer.Response)
}

class ActivationCodePresenter: ActivationCodePresentationLogic
{
    weak var viewController: ActivationCodeDisplayLogic?
    
    func presentInitializeViewResult(response : ActivationCode.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : ActivationCode.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : ActivationCode.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : ActivationCode.Validate.ViewModel(isValid: response.otp.length == 4))
    }
    
    func presentReloadPageResult(response : ActivationCode.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : ActivationCode.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : ActivationCode.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : ActivationCode.SendData.ViewModel(isSuccess: response.isSuccess, isExpired: response.isExpired))
    }
    
    func presentRemainingTimeResult(response: ActivationCode.Timer.Response) {
        viewController?.displayRemainingTimeResult(viewModel: ActivationCode.Timer.ViewModel(timeRemainingStr: response.timeRemaining.toTimeStr(), isResendButtonEnabled: response.timeRemaining == 0))
    }
}
