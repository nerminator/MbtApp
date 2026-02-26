//
//  LocationResultPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LocationResultPresentationLogic
{
    func presentInitializeViewResult(response : LocationResult.Initialize.Response)
    func presentValidateInputs(response : LocationResult.Validate.Response)
    func presentReloadPageResult(response : LocationResult.Reload.Response)
    func presentSendDataResult(response : LocationResult.SendData.Response)
}

class LocationResultPresenter: LocationResultPresentationLogic
{
    weak var viewController: LocationResultDisplayLogic?
    
    func presentInitializeViewResult(response : LocationResult.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : LocationResult.Initialize.ViewModel(selectedBuilding: response.selectedBuilding, selectedRoomIndex: response.selectedRoomIndex))
    }
    
    func presentValidateInputs(response : LocationResult.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : LocationResult.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : LocationResult.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : LocationResult.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : LocationResult.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : LocationResult.SendData.ViewModel())
    }
}
