//
//  ProfilePresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol ProfilePresentationLogic
{
    func presentInitializeViewResult(response : Profile.Initialize.Response)
    func presentValidateInputs(response : Profile.Validate.Response)
    func presentReloadPageResult(response : Profile.Reload.Response)
    func presentSendDataResult(response : Profile.SendData.Response)
}

class ProfilePresenter: ProfilePresentationLogic
{
    weak var viewController: ProfileDisplayLogic?
    
    func presentInitializeViewResult(response : Profile.Initialize.Response) {
        
        viewController?.displayInitializeViewResult(viewModel : Profile.Initialize.ViewModel(nameSurname: response.profileResponse?.nameSurname, flexibleTitle: response.profileResponse?.workHoursText, title: response.profileResponse?.title, officeLocation: response.profileResponse?.officeLocation, organizationUnit: response.profileResponse?.organizationUnit, manager: response.profileResponse?.manager, registerNumber: response.profileResponse?.registerNumber, flexibleAvailable: response.profileResponse?.workHoursAvailable ?? false ,isSuccess: response.profileResponse != nil))
    }
    
    func presentValidateInputs(response : Profile.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : Profile.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : Profile.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : Profile.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : Profile.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : Profile.SendData.ViewModel())
    }
}
