//
//  MBTSingleton.swift
//  MBT
//
//  Created by Serkut Yegin on 2.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class MBTSingleton: NSObject {
    
    static let shared = MBTSingleton()
    
    var isFirstOpen : Bool {
        return !UserDefaults.standard.bool(forKey: MBTConstants.UserPreference.TermsOfUseShown)
    }
    
    var deviceTokenForPush : String? {
        didSet {
            if let deviceToken = deviceTokenForPush, !TokenManager.sharedManager.accessToken.isEmpty {
                WSProvider.shared.wsRequest(.saveDeviceInfo(deviceToken: deviceToken))
            }
        }
    }
}

extension MBTSingleton {
    
    func clearUser() {
        TokenManager.sharedManager.clearAccessToken()
    }
    
    func activateUser() {
        (UIApplication.shared.delegate as? AppDelegate)?.registerNotification()
        WSProvider.shared.wsRequest(.getNotificationBadgeCount) { (response : WSResponse<Int>) in
            if response.isSuccess { MBTNotificationManager.shared.unreadedNotificationCount = response.data ?? 0 }
        }
    }
    
    func setTermsOfUseShown() {
        UserDefaults.standard.set(true, forKey: MBTConstants.UserPreference.TermsOfUseShown)
    }
    
}
