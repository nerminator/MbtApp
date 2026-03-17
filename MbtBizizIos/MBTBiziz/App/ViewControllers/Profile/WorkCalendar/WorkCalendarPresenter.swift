//
//  WorkCalendarPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol WorkCalendarPresentationLogic
{
    func presentInitializeViewResult(response : WorkCalendar.Initialize.Response)
    func presentValidateInputs(response : WorkCalendar.Validate.Response)
    func presentReloadPageResult(response : WorkCalendar.Reload.Response)
    func presentSendDataResult(response : WorkCalendar.SendData.Response)
}

class WorkCalendarPresenter: WorkCalendarPresentationLogic
{
    weak var viewController: WorkCalendarDisplayLogic?
    
    func presentInitializeViewResult(response : WorkCalendar.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : WorkCalendar.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : WorkCalendar.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : WorkCalendar.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : WorkCalendar.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : WorkCalendar.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : WorkCalendar.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : WorkCalendar.SendData.ViewModel())
    }
}
