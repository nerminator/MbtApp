//
//  AboutPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol AboutPresentationLogic
{
    func presentInitializeViewResult(response : About.Initialize.Response)
    func presentValidateInputs(response : About.Validate.Response)
    func presentReloadPageResult(response : About.Reload.Response)
    func presentSendDataResult(response : About.SendData.Response)
}

class AboutPresenter: AboutPresentationLogic
{
    weak var viewController: AboutDisplayLogic?
    
    func presentInitializeViewResult(response : About.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : About.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : About.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : About.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : About.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : About.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : About.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : About.SendData.ViewModel())
    }
}
