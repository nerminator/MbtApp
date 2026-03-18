//
//  FlexibleWorkInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol FlexibleWorkBusinessLogic
{
    func initializeView(request : FlexibleWork.Initialize.Request)
    func validateInputs(request : FlexibleWork.Validate.Request)
    func reloadPage(request : FlexibleWork.Reload.Request)
    func sendData(request : FlexibleWork.SendData.Request)
    
    var selectedYear : Int? { get set }
    var yearlyWorkHours : [MBTFlexibleYearlyMonthList] { get set }
    var plusKey : String? { get set }
    var minusKey : String? { get set }
    
    func getNavigationTitle() -> String?
    func getNavigationSmallTitle() -> String?
}

protocol FlexibleWorkDataStore
{
    var navigationTitle : String? { get set }
    var navigationTitleSmall : String? { get set }
}

class FlexibleWorkInteractor: FlexibleWorkBusinessLogic, FlexibleWorkDataStore
{
    var presenter: FlexibleWorkPresentationLogic?
    var worker = FlexibleWorkWorker()
    var selectedYear: Int?
    var yearlyWorkHours: [MBTFlexibleYearlyMonthList] = []
    var plusKey: String?
    var minusKey: String?
    var navigationTitle: String?
    var navigationTitleSmall: String?
    
    func getNavigationTitle() -> String? {
        return navigationTitle
    }
    func getNavigationSmallTitle() -> String? {
        return navigationTitleSmall
    }
    
    func initializeView(request : FlexibleWork.Initialize.Request) {
        
        worker.getYearlyWorkHours(selectedYear) { [weak self] (response) in
            guard let response = response else { return }
            self?.selectedYear = response.selectedYear
            self?.yearlyWorkHours = response.monthList ?? []
            self?.plusKey = response.plusHoursText
            self?.minusKey = response.minusHoursText
            self?.presenter?.presentInitializeViewResult(response: FlexibleWork.Initialize.Response(selectedYear: response.selectedYear, years: response.yearList))
        }
    }
    
    func validateInputs(request : FlexibleWork.Validate.Request) {
        presenter?.presentValidateInputs(response: FlexibleWork.Validate.Response())
    }
    
    func reloadPage(request : FlexibleWork.Reload.Request) {
        selectedYear = request.selectedYear.toInt()
        worker.getYearlyWorkHours(selectedYear) { [weak self] (response) in
            self?.yearlyWorkHours = response?.monthList ?? []
            self?.presenter?.presentReloadPageResult(response : FlexibleWork.Reload.Response())
        }
    }
    
    func sendData(request : FlexibleWork.SendData.Request) {
        presenter?.presentSendDataResult(response : FlexibleWork.SendData.Response())
    }
  
}
