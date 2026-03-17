//
//  SettingsPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 31.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol SettingsPresentationLogic
{
    func presentInitializeViewResult(response : Settings.Initialize.Response)
    func presentValidateInputs(response : Settings.Validate.Response)
    func presentReloadPageResult(response : Settings.Reload.Response)
    func presentSendDataResult(response : Settings.SendData.Response)
}

class SettingsPresenter: SettingsPresentationLogic
{
    weak var viewController: SettingsDisplayLogic?
    
    func presentInitializeViewResult(response : Settings.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : Settings.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : Settings.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : Settings.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : Settings.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : Settings.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : Settings.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : Settings.SendData.ViewModel())
    }
}
