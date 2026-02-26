//
//  NewsDetailPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol NewsDetailPresentationLogic
{
    func presentInitializeViewResult(response : NewsDetail.Initialize.Response)
    func presentValidateInputs(response : NewsDetail.Validate.Response)
    func presentReloadPageResult(response : NewsDetail.Reload.Response)
    func presentSendDataResult(response : NewsDetail.SendData.Response)
    func presentGetUserDiscountResult(response : MBTGetDiscountCodeResponse)
    func presentNoDiscountResult()
}

class NewsDetailPresenter: NewsDetailPresentationLogic
{
    weak var viewController: NewsDetailDisplayLogic?
    
    func presentInitializeViewResult(response : NewsDetail.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : NewsDetail.Initialize.ViewModel(title: response.title))
    }
    
    func presentValidateInputs(response : NewsDetail.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : NewsDetail.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : NewsDetail.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : NewsDetail.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : NewsDetail.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : NewsDetail.SendData.ViewModel())
    }
    
    func presentGetUserDiscountResult(response : MBTGetDiscountCodeResponse) {
        viewController?.displayGetUserCodeResult(response : response)
    }
    
    func presentNoDiscountResult(){
        viewController?.displayNoDiscountCodeResult()
    }
}
