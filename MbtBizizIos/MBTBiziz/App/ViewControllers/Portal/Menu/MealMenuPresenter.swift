//
//  MealMenuPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol MealMenuPresentationLogic
{
    func presentInitializeViewResult(response : MealMenu.Initialize.Response)
    func presentValidateInputs(response : MealMenu.Validate.Response)
    func presentReloadPageResult(response : MealMenu.Reload.Response)
    func presentSendDataResult(response : MealMenu.SendData.Response)
}

class MealMenuPresenter: MealMenuPresentationLogic
{
    weak var viewController: MealMenuDisplayLogic?
    
    func presentInitializeViewResult(response : MealMenu.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : MealMenu.Initialize.ViewModel(densityInfo: response.densityInfo, densityHeight: response.densityHeight))
    }
    
    func presentValidateInputs(response : MealMenu.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : MealMenu.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : MealMenu.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : MealMenu.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : MealMenu.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : MealMenu.SendData.ViewModel())
    }
}
