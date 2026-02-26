//
//  ActivationCodeWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class ActivationCodeWorker
{
    func doSetup(_ viewController : ActivationCodeViewController) {
        let interactor = ActivationCodeInteractor()
        let presenter = ActivationCodePresenter()
        let router = ActivationCodeRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func makeLogin(_ phoneNumber : String, otp: Int, completion:@escaping(_ response : MBTLoginResponse?, _ isExpired: Bool)->()) {

        WSProvider.shared.wsRequest(.login(phoneNumber: phoneNumber, pin: otp), success: { (response: WSResponse<MBTLoginResponse>) in
            completion(response.data, response.statusCode == .errorWithMessage)
        }) { (error) in
            switch error {
            case .internalError(let statusCode, _):
                completion(nil, statusCode == .errorWithMessage)
            default:
                completion(nil, false)
            }
        }
    }
    
    func resendActivation(_ phoneNumber : String, completion:@escaping(_ isSuccess:Bool)->()) {

        WSProvider.shared.wsRequest(.checkPhone(phoneNumber: phoneNumber), success: { (response : WSResponse<MarshalResponse>) in
            completion(response.isSuccess)
        }) { (error) in
            completion(false)
        }
    }
}
