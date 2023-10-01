//
//  SettingsWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 31.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class SettingsWorker
{
    func doSetup(_ viewController : SettingsViewController) {
        let interactor = SettingsInteractor()
        let presenter = SettingsPresenter()
        let router = SettingsRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    func getNotificationSettings(_ completion : @escaping (_ response : MBTSettingsResponse?)->()) {
        WSProvider.shared.wsRequest(.getNotificationSettings, map: completion)
    }
    
    func changeNotificationSetting(_ type:Int, value:Int, completion: @escaping (_ isSuccess : Bool) ->()) {
        
        WSProvider.shared.wsRequest(.changeNotificationSetting(type: type, value: value), success: { (response : WSResponse<MarshalResponse>) in
            completion(response.isSuccess)
        }) { (error) in
            completion(false)
        }
        
    }
    
    func signout(_ completion: @escaping () ->()) {
        if let deviceToken = MBTSingleton.shared.deviceTokenForPush {
            WSProvider.shared.wsRequest(.deleteDeviceInfo(deviceToken: deviceToken))
        }
        WSProvider.shared.wsRequest(.signOut, success: { (_ response : WSResponse<MarshalResponse>) in
            completion()
        }) { (error) in
            completion()
        }
    }
}
