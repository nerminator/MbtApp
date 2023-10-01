//
//  NetworkAPI.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Moya

enum NetworkAPI {

    #if DEBUG
    static var baseUrl : ServiceUrl = .live
    #else
    static var baseUrl : ServiceUrl = .live
    #endif
    
    case initCall(versionNumber: String)
    case checkPhone(phoneNumber:String)
    case login(phoneNumber:String, pin:Int)
    case signOut
    case getNewsList(type:NewsType, discountType:DiscountType?, pageNumber : Int)
    case getNewsDetail(identifier: Int)
    case getBirthdayList
    case getFoodMenu
    case getLocations
    case getProfile
    case getYearlyWorkHours(year:Int)
    case getMonthlyWorkHours(year:Int, month:Int)
    case getWorkCalendar(date:String)
    case getShuttleOptions
    case getShuttleList(type:Int,companyLocationId:Int)
    case getNotificationSettings
    case changeNotificationSetting(type:Int, value:Int)
    
    case saveDeviceInfo(deviceToken:String)
    case deleteDeviceInfo(deviceToken:String)
    case getNotificationList(firstDate:String?,lastDate:String?)
    case deleteNotification(notificationId:Int)
    case getNotificationBadgeCount

    case sendQRCode(code: String)
    case getCaptcha
    case getUserConfig
}

enum ServiceUrl : String {
    
    case internalTest = "http://78.135.113.30/bizizBackend/public/index.php/api/v1"
    case externalTest = "a"
    case live = "https://bizizapp.com/bizizBackend/public/index.php/api/v1"
    
    mutating func switchUrl() -> String {
        switch self {
        case .internalTest: self = .externalTest; break
        case .externalTest: self = .live; break
        case .live: self = .internalTest; break
        }
        return self.rawValue
    }
    
}
