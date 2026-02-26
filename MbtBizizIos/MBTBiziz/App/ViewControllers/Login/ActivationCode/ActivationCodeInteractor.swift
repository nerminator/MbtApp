//
//  ActivationCodeInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol ActivationCodeBusinessLogic
{
    func initializeView(request : ActivationCode.Initialize.Request)
    func validateInputs(request : ActivationCode.Validate.Request)
    func reloadPage(request : ActivationCode.Reload.Request)
    func sendData(request : ActivationCode.SendData.Request)
    
    func startTimer(request : ActivationCode.Timer.Request)
}

protocol ActivationCodeDataStore
{
    var phoneNumber : String {get set}
}

class ActivationCodeInteractor: ActivationCodeBusinessLogic, ActivationCodeDataStore
{
    var presenter: ActivationCodePresentationLogic?
    var worker = ActivationCodeWorker()
    var phoneNumber: String = ""
    
    fileprivate var timeRemaining : Int = 60 {
        didSet {
            presenter?.presentRemainingTimeResult(response: ActivationCode.Timer.Response(timeRemaining: timeRemaining))
            if timeRemaining == 0 {
                stopTimer()
            }
        }
    }
    
    fileprivate var timer : Timer?
    
    func initializeView(request : ActivationCode.Initialize.Request) {
        presenter?.presentInitializeViewResult(response: ActivationCode.Initialize.Response())
    }
    
    func validateInputs(request : ActivationCode.Validate.Request) {
        presenter?.presentValidateInputs(response: ActivationCode.Validate.Response(otp: request.otp))
    }
    
    func reloadPage(request : ActivationCode.Reload.Request) {
        stopTimer()
        worker.resendActivation(phoneNumber) { [weak self] (isSuccess) in
            if isSuccess {
                self?.startTimer(request: ActivationCode.Timer.Request())
                self?.presenter?.presentReloadPageResult(response : ActivationCode.Reload.Response())
            }
        }
    }
    
    func sendData(request : ActivationCode.SendData.Request) {

     /*   worker.makeLogin(phoneNumber, otp: request.otp) { [weak self] (response, isExpired) in

            guard let response = response, let token = response.token, !token.isEmpty else {
                self?.presenter?.presentSendDataResult(response : ActivationCode.SendData.Response(isSuccess: false, isExpired: isExpired))
                return
            }
            TokenManager.sharedManager.handleLoginResponse(response)
            self?.presenter?.presentSendDataResult(response : ActivationCode.SendData.Response(isSuccess: true, isExpired: isExpired))
        }*/
    }
    
    func startTimer(request: ActivationCode.Timer.Request) {
        timeRemaining = 60
        stopTimer()
        timer = Timer.runThisEvery(seconds: 1, handler: { [weak self] (timer) in
            guard let remainingTime = self?.timeRemaining else { return }
            self?.timeRemaining = max(0, remainingTime - 1)
        })
    }
    
    fileprivate func stopTimer() {
        timer?.invalidate()
        timer = nil
    }
    
    deinit {
        stopTimer()
    }
  
}
