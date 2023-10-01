//
//  NotificationsPresenter.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol NotificationsPresentationLogic
{
    func presentInitializeViewResult(response : Notifications.Initialize.Response)
    func presentValidateInputs(response : Notifications.Validate.Response)
    func presentUpdatePageResult(response : Notifications.Update.Response)
    func presentReloadPageResult(response : Notifications.Reload.Response)
    func presentSendDataResult(response : Notifications.SendData.Response)
}

class NotificationsPresenter: NotificationsPresentationLogic
{
    weak var viewController: NotificationsDisplayLogic?
    
    func presentInitializeViewResult(response : Notifications.Initialize.Response) {
        viewController?.displayInitializeViewResult(viewModel : Notifications.Initialize.ViewModel())
    }
    
    func presentValidateInputs(response : Notifications.Validate.Response) {
        viewController?.displayValidateInputs(viewModel : Notifications.Validate.ViewModel())
    }
    
    func presentReloadPageResult(response : Notifications.Reload.Response) {
        viewController?.displayReloadPageResult(viewModel : Notifications.Reload.ViewModel())
    }
    
    func presentSendDataResult(response : Notifications.SendData.Response) {
        viewController?.displaySendDataResult(viewModel : Notifications.SendData.ViewModel(index: response.index))
    }
    
    func presentUpdatePageResult(response: Notifications.Update.Response) {
        viewController?.displayUpdatePageResult(viewModel: Notifications.Update.ViewModel(numberOfNewNotifications: response.numberOfNewNotifications))
    }
}
