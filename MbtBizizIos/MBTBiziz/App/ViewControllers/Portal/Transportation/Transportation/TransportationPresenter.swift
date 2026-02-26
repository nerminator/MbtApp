//
//  TransportationPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationPresentationLogic
{
    func presentInitializeViewResult(response : Transportation.Initialize.Response)
    func presentValidateInputs(response : Transportation.Validate.Response)
    func presentReloadPageResult(response : Transportation.Reload.Response)
    func presentSendDataResult(response : Transportation.SendData.Response)
    func presentSwitchLocationResult(response : Transportation.SwitchLocations.Response)
    func presentBecomeFirstResponderResult(response : Transportation.BecomeFirstResponder.Response)
    func presentMakeSearchResult(response : Transportation.SearchResult.Response)
    func presentClearTextFieldResult(response : Transportation.ClearTextField.Response)
}

class TransportationPresenter: TransportationPresentationLogic
{
    weak var viewController: TransportationDisplayLogic?
    
    func presentInitializeViewResult(response : Transportation.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : Transportation.Initialize.ViewModel(tabItems: response.tabItems, shuttleList: response.shuttleList, arrivalText: response.arrivalText))
    }
    
    func presentValidateInputs(response : Transportation.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : Transportation.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : Transportation.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : Transportation.Reload.ViewModel(shuttleList: response.shuttleList))
    }
    
    func presentSendDataResult(response : Transportation.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : Transportation.SendData.ViewModel())
    }
    
    func presentSwitchLocationResult(response: Transportation.SwitchLocations.Response) {
        viewController?.displaySwitchLocationResult(viewModel: Transportation.SwitchLocations.ViewModel(tabItems: response.tabItems, shuttleList: response.shuttleList))
    }
    
    func presentBecomeFirstResponderResult(response: Transportation.BecomeFirstResponder.Response) {
        viewController?.displayBecomeFirstResponder(viewModel: Transportation.BecomeFirstResponder.ViewModel(needToRouteSearch: response.needToRouteSearch, shuttleList: response.shuttleList))
    }
    
    func presentMakeSearchResult(response: Transportation.SearchResult.Response) {
        viewController?.displayMakeSearchResult(viewModel: Transportation.SearchResult.ViewModel(shuttleList: response.shuttleList, arrivalText: response.arrivalText, departureText: response.departureText))
    }
    
    func presentClearTextFieldResult(response: Transportation.ClearTextField.Response) {
        viewController?.displayClearTextFieldResult(viewModel: Transportation.ClearTextField.ViewModel(shuttleList: response.shuttleList, arrivalText: response.arrivalText, departureText: response.departureText))
    }
}
