//
//  ProfileInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol ProfileBusinessLogic
{
    func initializeView(request : Profile.Initialize.Request)
    func validateInputs(request : Profile.Validate.Request)
    func reloadPage(request : Profile.Reload.Request)
    func sendData(request : Profile.SendData.Request)
    
    var yearlyWorkHoursText : String? { get }
    var monthlyWorkHoursText : String? { get }
}

protocol ProfileDataStore
{

}

class ProfileInteractor: ProfileBusinessLogic, ProfileDataStore
{
    var presenter: ProfilePresentationLogic?
    var worker = ProfileWorker()
    fileprivate var profileData : MBTProfileResponse?
    var yearlyWorkHoursText: String? {
        return profileData?.yearlyWorkHoursText
    }
    var monthlyWorkHoursText: String? {
        return profileData?.monthlyWorkHoursText
    }
    
    func initializeView(request : Profile.Initialize.Request) {
        if profileData == nil {
            worker.getProfile { [weak self] (response) in
                if let data = response {
                    self?.profileData = data
                    self?.presenter?.presentInitializeViewResult(response: Profile.Initialize.Response(profileResponse: response))
                }
            }
        }
    }
    
    func validateInputs(request : Profile.Validate.Request) {
        presenter?.presentValidateInputs(response: Profile.Validate.Response())
    }
    
    func reloadPage(request : Profile.Reload.Request) {
        presenter?.presentReloadPageResult(response : Profile.Reload.Response())
    }
    
    func sendData(request : Profile.SendData.Request) {
        presenter?.presentSendDataResult(response : Profile.SendData.Response())
    }
  
}

