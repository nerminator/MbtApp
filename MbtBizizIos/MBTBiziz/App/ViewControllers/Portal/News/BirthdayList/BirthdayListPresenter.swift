//
//  BirthdayListPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol BirthdayListPresentationLogic
{
    func presentInitializeViewResult(response : BirthdayList.Initialize.Response)
    func presentValidateInputs(response : BirthdayList.Validate.Response)
    func presentReloadPageResult(response : BirthdayList.Reload.Response)
    func presentSendDataResult(response : BirthdayList.SendData.Response)
}

class BirthdayListPresenter: BirthdayListPresentationLogic
{
    weak var viewController: BirthdayListDisplayLogic?
    
    func presentInitializeViewResult(response : BirthdayList.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : BirthdayList.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : BirthdayList.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : BirthdayList.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : BirthdayList.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : BirthdayList.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : BirthdayList.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : BirthdayList.SendData.ViewModel())
    }
}
