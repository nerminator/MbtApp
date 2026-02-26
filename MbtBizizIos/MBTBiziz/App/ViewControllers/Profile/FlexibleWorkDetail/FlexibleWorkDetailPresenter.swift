//
//  FlexibleWorkDetailPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 20.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol FlexibleWorkDetailPresentationLogic
{
    func presentInitializeViewResult(response : FlexibleWorkDetail.Initialize.Response)
    func presentValidateInputs(response : FlexibleWorkDetail.Validate.Response)
    func presentReloadPageResult(response : FlexibleWorkDetail.Reload.Response)
    func presentSendDataResult(response : FlexibleWorkDetail.SendData.Response)
}

class FlexibleWorkDetailPresenter: FlexibleWorkDetailPresentationLogic
{
    weak var viewController: FlexibleWorkDetailDisplayLogic?
    
    func presentInitializeViewResult(response : FlexibleWorkDetail.Initialize.Response) {
        let yearStr = "\(response.year)"
        let monthStr = response.month.monthStr
        var yearMonthStr = monthStr + " " + yearStr
        if let addition = response.navbarTitleAddition {
            yearMonthStr = yearMonthStr + " " + addition
        }
        viewController?.displayInitializeViewResult(viewModel : FlexibleWorkDetail.Initialize.ViewModel(yearMonthStr: yearMonthStr))
    }
    
    func presentValidateInputs(response : FlexibleWorkDetail.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : FlexibleWorkDetail.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : FlexibleWorkDetail.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : FlexibleWorkDetail.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : FlexibleWorkDetail.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : FlexibleWorkDetail.SendData.ViewModel())
    }
}
