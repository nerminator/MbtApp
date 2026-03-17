//
//  NetworkAPI.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Moya

enum AppEnvironment {
    case dev
    case staging
    case production

    static var current: AppEnvironment {
        let executableName = Bundle.main.object(forInfoDictionaryKey: "CFBundleExecutable") as? String ?? ""
        let bundleIdentifier = Bundle.main.bundleIdentifier ?? ""

        if executableName.contains("-Dev") {
            return .dev
        }

        if executableName.contains("-Production") || executableName.contains("-Prod") || bundleIdentifier == "com.daimlertruck.dtag.internal.ios.mbt.test" {
            return .production
        }

        return .staging
    }
}

enum NetworkAPI {

    #if DEBUG
        static var baseUrl : ServiceUrl = .live
    #else
        static var baseUrl : ServiceUrl = .live
    #endif

    static var resolvedBaseUrlString: String {
        switch AppEnvironment.current {
        case .dev:
            return "http://localhost:8000/api/v1/"
        case .staging:
            return "https://biziapp-test.app.daimlertruck.com/bizizBackend/public/index.php/api/v1/"
        case .production:
            return "https://bizizapp.com/bizizBackend/public/index.php/api/v1/"
        }
    }
    
    case initCall(versionNumber: String)
    case checkPhone(phoneNumber:String)
    case login(phoneNumber:String, pin:Int)
    case signOut
    case getNewsList(type:NewsType, discountType:DiscountType?, locId:Int?, pageNumber : Int)
    case getNewsDetail(identifier: Int)
    case getDiscountCode(identifier: Int)
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
    case getClubLocs
    case getPhoneLocs
    case getClubs(loc_id:Int)
    case getPhones(loc_id:Int)
    case getMedias
    case submitFeedback(text: String)
    case appStartup
    
    case payslipRequestOtp              // POST /payslip/request-otp
    case payslipIsActive                // GET /payslip/isActive
    case payslipVerifyOtp(code: String) // POST /payslip/verify-otp
    case payslipFetch(year: Int, month: Int) // POST /payslip/fetch
    
    case getUserBusinessCardState
    case activateDigitalCard
    case deactivateDigitalCard
    case menuIncrement(keyName: String)
}

enum ServiceUrl : String {
    
    case internalTest = "http://78.135.113.30/bizizBackend/public/index.php/api/v1"
    case externalTest = "a"
    case dev = "http://192.168.2.156:8000/index.php/api/v1"
    case live = "https://bizizapp.com/bizizBackend/public/index.php/api/v1"
    
    
    mutating func switchUrl() -> String {
        switch self {
        case .internalTest: self = .externalTest; break
        case .externalTest: self = .live; break
        case .live: self = .internalTest; break
        case .dev: self = .internalTest; break
        }
        return self.rawValue
    }
    
}
