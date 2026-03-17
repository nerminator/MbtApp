//
//  TermOfUsePresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TermOfUsePresentationLogic
{
    func presentInitializeViewResult(response : TermOfUse.Initialize.Response)
    func presentValidateInputs(response : TermOfUse.Validate.Response)
    func presentReloadPageResult(response : TermOfUse.Reload.Response)
    func presentSendDataResult(response : TermOfUse.SendData.Response)
}

class TermOfUsePresenter: TermOfUsePresentationLogic
{
    weak var viewController: TermOfUseDisplayLogic?
    
    func presentInitializeViewResult(response : TermOfUse.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : TermOfUse.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : TermOfUse.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : TermOfUse.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : TermOfUse.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : TermOfUse.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : TermOfUse.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : TermOfUse.SendData.ViewModel())
    }
}
