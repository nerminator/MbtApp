//
//  NewsPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol NewsPresentationLogic
{
    func presentInitializeViewResult(response : News.Initialize.Response)
    func presentValidateInputs(response : News.Validate.Response)
    func presentReloadPageResult(response : News.Reload.Response)
    func presentSendDataResult(response : News.SendData.Response)
}

class NewsPresenter: NewsPresentationLogic
{
    weak var viewController: NewsDisplayLogic?
    
    func presentInitializeViewResult(response : News.Initialize.Response) {
        var pickerModel : CommonPickerModel?
        if response.newsType == .indirim {
            pickerModel = UIPickerView.commonPickerWithToolbar
        }
        viewController?.displayInitializeViewResult(viewModel : News.Initialize.ViewModel(newsType: response.newsType, pickerModel: pickerModel))
    }
    
    func presentValidateInputs(response : News.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : News.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : News.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : News.Reload.ViewModel(birthdayCount: response.birthdayCount))
    }
    
    func presentSendDataResult(response : News.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : News.SendData.ViewModel())
    }
}
