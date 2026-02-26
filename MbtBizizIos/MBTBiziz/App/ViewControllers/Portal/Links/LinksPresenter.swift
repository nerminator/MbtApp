//
//  LinksPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LinksPresentationLogic
{
  /*  func presentInitializeViewResult(response : Links.Initialize.Response)
    func presentValidateInputs(response : Links.Validate.Response)
    func presentReloadPageResult(response : Links.Reload.Response)
    func presentSendDataResult(response : Links.SendData.Response)
    func presentGetMealInfoResult(response : Links.MealInfo.Response)*/
}

class LinksPresenter: LinksPresentationLogic
{
    weak var viewController: LinksDisplayLogic?
    
  /*  func presentInitializeViewResult(response : Links.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : Home.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : Links.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : Home.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : Links.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : Home.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : Links.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : Home.SendData.ViewModel())
    }
    
    func presentGetMealInfoResult(response: Links.MealInfo.Response) {
        viewController?.displayMealInfoResult(viewModel: Home.MealInfo.ViewModel(mealInfo: response.mealInfo))
    }*/
}
