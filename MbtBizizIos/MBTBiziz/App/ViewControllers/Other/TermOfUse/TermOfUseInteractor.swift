//
//  TermOfUseInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TermOfUseBusinessLogic
{
    func initializeView(request : TermOfUse.Initialize.Request)
    func validateInputs(request : TermOfUse.Validate.Request)
    func reloadPage(request : TermOfUse.Reload.Request)
    func sendData(request : TermOfUse.SendData.Request)
}

protocol TermOfUseDataStore
{

}

class TermOfUseInteractor: TermOfUseBusinessLogic, TermOfUseDataStore
{
    var presenter: TermOfUsePresentationLogic?
    var worker = TermOfUseWorker()
    
    func initializeView(request : TermOfUse.Initialize.Request) {
        presenter?.presentInitializeViewResult(response: TermOfUse.Initialize.Response())
    }
    
    func validateInputs(request : TermOfUse.Validate.Request) {
        presenter?.presentValidateInputs(response: TermOfUse.Validate.Response())
    }
    
    func reloadPage(request : TermOfUse.Reload.Request) {
        presenter?.presentReloadPageResult(response : TermOfUse.Reload.Response())
    }
    
    func sendData(request : TermOfUse.SendData.Request) {
        presenter?.presentSendDataResult(response : TermOfUse.SendData.Response())
    }
  
}
