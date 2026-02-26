//
//  BirthdayListInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol BirthdayListBusinessLogic
{
    func initializeView(request : BirthdayList.Initialize.Request)
    func validateInputs(request : BirthdayList.Validate.Request)
    func reloadPage(request : BirthdayList.Reload.Request)
    func sendData(request : BirthdayList.SendData.Request)
    
    var birthdayList : [MBTBirthdayBirthdayList] { get set }
    var dateInfo : String? { get set }
}

protocol BirthdayListDataStore
{

}

class BirthdayListInteractor: BirthdayListBusinessLogic, BirthdayListDataStore
{
    var presenter: BirthdayListPresentationLogic?
    var worker = BirthdayListWorker()
    var birthdayList: [MBTBirthdayBirthdayList] = []
    var dateInfo: String?
    
    func initializeView(request : BirthdayList.Initialize.Request) {
        presenter?.presentInitializeViewResult(response: BirthdayList.Initialize.Response())
    }
    
    func validateInputs(request : BirthdayList.Validate.Request) {
        presenter?.presentValidateInputs(response: BirthdayList.Validate.Response())
    }
    
    func reloadPage(request : BirthdayList.Reload.Request) {
        presenter?.presentReloadPageResult(response : BirthdayList.Reload.Response())
    }
    
    func sendData(request : BirthdayList.SendData.Request) {
        worker.getBirthdayList { [weak self] (response) in
            self?.birthdayList = response?.birthdayList ?? []
            self?.dateInfo = response?.dateInfo
            self?.presenter?.presentSendDataResult(response : BirthdayList.SendData.Response())
        }
    }
  
}
