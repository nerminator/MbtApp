//
//  LocationPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LocationPresentationLogic
{
    func presentInitializeViewResult(response : Location.Initialize.Response)
    func presentValidateInputs(response : Location.Validate.Response)
    func presentReloadPageResult(response : Location.Reload.Response)
    func presentSendDataResult(response : Location.SendData.Response)
}

class LocationPresenter: LocationPresentationLogic
{
    weak var viewController: LocationDisplayLogic?
    
    func presentInitializeViewResult(response : Location.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : Location.Initialize.ViewModel(selectedBuilding: response.selectedBuilding, initialSegmentIndex: response.initialSegmentIndex))
    }
    
    func presentValidateInputs(response : Location.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : Location.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : Location.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : Location.Reload.ViewModel(selectedBuilding: response.selectedBuilding))
    }
    
    func presentSendDataResult(response : Location.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : Location.SendData.ViewModel())
    }
}
