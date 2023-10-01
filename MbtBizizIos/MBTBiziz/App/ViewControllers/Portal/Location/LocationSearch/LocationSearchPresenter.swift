//
//  LocationSearchPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LocationSearchPresentationLogic
{
    func presentInitializeViewResult(response : LocationSearch.Initialize.Response)
    func presentValidateInputs(response : LocationSearch.Validate.Response)
    func presentReloadPageResult(response : LocationSearch.Reload.Response)
    func presentSendDataResult(response : LocationSearch.SendData.Response)
}

class LocationSearchPresenter: LocationSearchPresentationLogic
{
    weak var viewController: LocationSearchDisplayLogic?
    
    func presentInitializeViewResult(response : LocationSearch.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : LocationSearch.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : LocationSearch.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : LocationSearch.Validate.ViewModel(building: response.building, room: response.room))
    }
    
    func presentReloadPageResult(response : LocationSearch.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : LocationSearch.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : LocationSearch.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : LocationSearch.SendData.ViewModel())
    }
}
