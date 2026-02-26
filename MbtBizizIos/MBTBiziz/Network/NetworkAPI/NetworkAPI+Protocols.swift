//
//  NetworkAPI+Protocols.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol Authorization {
    var needAuthorization : Bool { get }
    var needCridential : Bool { get }
}

protocol Loading {
    var needToShowLoading : Bool { get }
}

extension NetworkAPI : Authorization, Loading {

    var needAuthorization: Bool {
        switch self {
        case .checkPhone, .checkPhone, .login, .initCall, .appStartup:
            return false
        default: return true
        }
    }
    var needToShowLoading: Bool {
        switch self {
        
        case .getProfile,.getMonthlyWorkHours(_),
             .getWorkCalendar(_),.getNotificationSettings,.saveDeviceInfo(_),.deleteDeviceInfo(_),
             .getNotificationList(_),.deleteNotification(_),.getNotificationBadgeCount, .initCall, .getCaptcha, .getUserConfig, .appStartup :
            return false
        default:
            return true
        }
    }
    
    var needCridential: Bool {
        return false
    }
    
}
