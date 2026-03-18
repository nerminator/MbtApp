//
//  WelcomeInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol WelcomeBusinessLogic
{
    func initializeView(request : Welcome.Initialize.Request)
    func validateInputs(request : Welcome.Validate.Request)
    func reloadPage(request : Welcome.Reload.Request)
    func sendData(request : Welcome.SendData.Request)
}

protocol WelcomeDataStore
{

}

class WelcomeInteractor: WelcomeBusinessLogic, WelcomeDataStore
{
    var presenter: WelcomePresentationLogic?
    var worker = WelcomeWorker()

    private var sessionToken: String?
    
    func initializeView(request : Welcome.Initialize.Request) {
        presenter?.presentInitializeViewResult(response: Welcome.Initialize.Response())
    }
    
    func validateInputs(request : Welcome.Validate.Request) {
        presenter?.presentValidateInputs(response: Welcome.Validate.Response(phoneNumber: request.phoneNumber))
    }
    
    func reloadPage(request : Welcome.Reload.Request) {

    }
    
    func sendData(request : Welcome.SendData.Request) {

        //guard let token = sessionToken else { return }

        worker.checkPhone(request.phoneNumber) { [weak self] (isSuccess) in
            self?.presenter?.presentSendDataResult(response : Welcome.SendData.Response(isSuccess: isSuccess))
        }
    }
}
