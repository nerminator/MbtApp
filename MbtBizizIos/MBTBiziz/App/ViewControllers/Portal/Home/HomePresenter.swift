//
//  HomePresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol HomePresentationLogic
{
    func presentInitializeViewResult(response : Home.Initialize.Response)
    func presentValidateInputs(response : Home.Validate.Response)
    func presentReloadPageResult(response : Home.Reload.Response)
    func presentSendDataResult(response : Home.SendData.Response)
    func presentGetMealInfoResult(response : Home.MealInfo.Response)
}

class HomePresenter: HomePresentationLogic
{
    weak var viewController: HomeDisplayLogic?
    
    func presentInitializeViewResult(response : Home.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : Home.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : Home.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : Home.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : Home.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : Home.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : Home.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : Home.SendData.ViewModel())
    }
    
    func presentGetMealInfoResult(response: Home.MealInfo.Response) {
        viewController?.displayMealInfoResult(viewModel: Home.MealInfo.ViewModel(mealInfo: response.mealInfo))
    }
}
