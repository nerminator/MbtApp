//
//  TransportationSearchPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationSearchPresentationLogic
{
    func presentInitializeViewResult(response : TransportationSearch.Initialize.Response)
    func presentValidateInputs(response : TransportationSearch.Validate.Response)
    func presentReloadPageResult(response : TransportationSearch.Reload.Response)
    func presentSendDataResult(response : TransportationSearch.SendData.Response)
}

class TransportationSearchPresenter: TransportationSearchPresentationLogic
{
    weak var viewController: TransportationSearchDisplayLogic?
    
    func presentInitializeViewResult(response : TransportationSearch.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : TransportationSearch.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : TransportationSearch.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : TransportationSearch.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : TransportationSearch.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : TransportationSearch.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : TransportationSearch.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : TransportationSearch.SendData.ViewModel())
    }
}
