//
//  NavigationHelper.swift
//  MBT
//
//  Created by Serkut Yegin on 2.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class NavigationHelper {

    var tabbarVC : MBTTabBarController? {
        if let tabbar = rootVC.controller as? MBTTabBarController {
            return tabbar
        }
        return nil
    }
    
    var rootVC : MBTRootViewController {
        let appDelegate  = UIApplication.shared.delegate as! AppDelegate
        return appDelegate.window!.rootViewController as! MBTRootViewController
    }
    
    func showWelcomeScreen() {
        rootVC.showWelcomeScreen()
        MBTSingleton.shared.clearUser()
    }
    
    func showHomeScreen(/*baseResponse:MBTBaseResponse? = nil*/) {
        DispatchQueue.main.async {
            MBTSingleton.shared.activateUser()
            self.rootVC.showHomeScreen(/*baseResponse: baseResponse*/)
        }
    }
    
    internal func showBasicAlert(with title:String? = "TXT_COMMON_INFO".localized(), message:String?, actionTitles:[String] = [], cancelTitle:String = "TXT_COMMON_DONE".localized()) {
        guard let message = message, message.length > 0 else { return }
        let controller = UIAlertController(title: title, message: message, preferredStyle: .alert)
        for actionTitle in actionTitles {
            controller.addAction(UIAlertAction(title: actionTitle, style: .default, handler: nil))
        }
        controller.addAction(UIAlertAction(title: cancelTitle, style: .cancel, handler: nil))
        rootVC.presented.present(controller, animated: true, completion: nil)
    }
}
