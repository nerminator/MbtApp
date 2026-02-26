//
//  AboutInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol AboutBusinessLogic
{
    func initializeView(request : About.Initialize.Request)
    func validateInputs(request : About.Validate.Request)
    func reloadPage(request : About.Reload.Request)
    func sendData(request : About.SendData.Request)
}

protocol AboutDataStore
{

}

class AboutInteractor: AboutBusinessLogic, AboutDataStore
{
    var presenter: AboutPresentationLogic?
    var worker = AboutWorker()
    
    func initializeView(request : About.Initialize.Request) {
        presenter?.presentInitializeViewResult(response: About.Initialize.Response())
    }
    
    func validateInputs(request : About.Validate.Request) {
        presenter?.presentValidateInputs(response: About.Validate.Response())
    }
    
    func reloadPage(request : About.Reload.Request) {
        presenter?.presentReloadPageResult(response : About.Reload.Response())
    }
    
    func sendData(request : About.SendData.Request) {
        presenter?.presentSendDataResult(response : About.SendData.Response())
    }
  
}
