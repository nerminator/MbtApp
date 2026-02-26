//
//  FlexibleWorkDetailInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 20.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol FlexibleWorkDetailBusinessLogic
{
    func initializeView(request : FlexibleWorkDetail.Initialize.Request)
    func validateInputs(request : FlexibleWorkDetail.Validate.Request)
    func reloadPage(request : FlexibleWorkDetail.Reload.Request)
    func sendData(request : FlexibleWorkDetail.SendData.Request)
    
    var monthlyWorkHours : [MBTFlexibleMonthlyDayList] { get set }
    var plusKey : String? { get set }
    var minusKey : String? { get set }
}

protocol FlexibleWorkDetailDataStore
{
    var selectedYear : Int { get set }
    var selectedMonth : Int { get set }
    var navBarTitleAddition : String? { get set }
}

class FlexibleWorkDetailInteractor: FlexibleWorkDetailBusinessLogic, FlexibleWorkDetailDataStore
{
    var presenter: FlexibleWorkDetailPresentationLogic?
    var worker = FlexibleWorkDetailWorker()
    var selectedYear: Int = 0
    var selectedMonth: Int = 0
    var monthlyWorkHours: [MBTFlexibleMonthlyDayList] = []
    var plusKey: String?
    var minusKey: String?
    var navBarTitleAddition: String?
    
    func initializeView(request : FlexibleWorkDetail.Initialize.Request) {
        presenter?.presentInitializeViewResult(response: FlexibleWorkDetail.Initialize.Response(year: selectedYear, month: selectedMonth, navbarTitleAddition: navBarTitleAddition))
        reloadPage(request: FlexibleWorkDetail.Reload.Request())
    }
    
    func validateInputs(request : FlexibleWorkDetail.Validate.Request) {
        presenter?.presentValidateInputs(response: FlexibleWorkDetail.Validate.Response())
    }
    
    func reloadPage(request : FlexibleWorkDetail.Reload.Request) {
        worker.getMonthlyWorkHours(selectedYear, month: selectedMonth) { [weak self] (response) in
            self?.monthlyWorkHours = response?.dayList ?? []
            self?.plusKey = response?.plusHoursText
            self?.minusKey = response?.minusHoursText
            self?.presenter?.presentReloadPageResult(response : FlexibleWorkDetail.Reload.Response())
        }
    }
    
    func sendData(request : FlexibleWorkDetail.SendData.Request) {
        presenter?.presentSendDataResult(response : FlexibleWorkDetail.SendData.Response())
    }
  
}
