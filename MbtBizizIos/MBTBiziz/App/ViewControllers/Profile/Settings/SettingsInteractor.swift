//
//  SettingsInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 31.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol SettingsBusinessLogic
{
    func initializeView(request : Settings.Initialize.Request)
    func validateInputs(request : Settings.Validate.Request)
    func reloadPage(parentVC:UIViewController, request : Settings.Reload.Request)
    func sendData(request : Settings.SendData.Request)
    
    var settingList : [MBTSettingsNotificationSettingList] { get }
}

protocol SettingsDataStore
{

}

class SettingsInteractor: SettingsBusinessLogic, SettingsDataStore
{
    var presenter: SettingsPresentationLogic?
    var worker = SettingsWorker()
    var settingList: [MBTSettingsNotificationSettingList] = []
    
    func initializeView(request : Settings.Initialize.Request) {
        worker.getNotificationSettings { [weak self] (response) in
            self?.settingList = response?.notificationSettingList ?? []
            self?.presenter?.presentInitializeViewResult(response: Settings.Initialize.Response())
        }
    }
    
    func validateInputs(request : Settings.Validate.Request) {
        presenter?.presentValidateInputs(response: Settings.Validate.Response())
    }
    
    func reloadPage(parentVC:UIViewController, request : Settings.Reload.Request) {
        worker.signout(parentVC, { [weak self] in
            self?.presenter?.presentReloadPageResult(response : Settings.Reload.Response())
        })
    }
    
    func sendData(request : Settings.SendData.Request) {
        guard let index = request.index, settingList.count > index else { return }
        let item = settingList[index]
        guard let itemId = item.type else { return }
        worker.changeNotificationSetting(itemId, value: request.isOn ? 1 : 0) { [weak self] (isSuccess) in
            if isSuccess {
                item.value = request.isOn ? 1 : 0
                self?.presenter?.presentSendDataResult(response : Settings.SendData.Response())
            }
        }
    }
  
}
