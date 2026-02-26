//
//  NetworkAPI+TargetType.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import Moya

extension NetworkAPI : TargetType {
    
    var baseURL: URL {
        return URL(string: NetworkAPI.baseUrl.rawValue)!
    }
    
    /*case saveDeviceInfo(deviceToken:String)
    case deleteDeviceInfo(deviceToken:String)
    case getNotificationList(firstDate:String?,lastDate:String?)
    case deleteNotification(notificationId:Int)
    case getNotificationBadgeCount*/
    
    var path: String {
        switch self {
        case .checkPhone(_): return "/checkPhone"
        case .login(_): return "/login"
        case .getNewsList(_): return "/newsList"
        case .getNewsDetail(let identifier): return "/newsDetail/\(identifier)"
        case .getDiscountCode(let identifier): return "/getDiscountCode/\(identifier)"
        case .getBirthdayList: return "/birthdayList"
        case .getFoodMenu: return "/foodMenu"
        case .getLocations: return "/maps"
        case .getProfile: return "/profile"
        case .getYearlyWorkHours(let year): return "/yearlyWorkHours/\(year)"
        case .getMonthlyWorkHours(let year, let month): return "/monthlyWorkHours/\(year)/\(month)"
        case .getWorkCalendar(let date): return "/workCalendar/\(date)"
        case .getShuttleList(_): return "/shuttleList"
        case .getShuttleOptions: return "/shuttleOptionList"
        case .getNotificationSettings: return "/notificationSettings"
        case .changeNotificationSetting(_): return "changeNotificationSetting"
        case .saveDeviceInfo(_): return "/saveDeviceInfo"
        case .deleteDeviceInfo(_): return "/deleteDeviceInfo"
        case .getNotificationList(_): return "/notificationList"
        case .deleteNotification(_): return "/deleteNotification"
        case .getNotificationBadgeCount: return "/notificationBadgeCount"
        case .signOut: return "/signOut"
        case .initCall: return "/init"
        case .sendQRCode: return "/sendQRCode"
        case .getCaptcha: return "/captcha"
        case .checkPhone: return "/checkPhone"
        case .getUserConfig: return "/userConfig"
        case .getClubLocs: return "/socialClubLocs"
        case .getPhoneLocs: return "/phoneLocs"
        case .getClubs(let loc_id): return "/socialClubs/\(loc_id)"
        case .getPhones(let loc_id): return "/phones/\(loc_id)"
        case .getMedias : return "/medias"
        case .submitFeedback: return "/submitFeedback"
        case .appStartup: return "/appStartup"
        case .payslipRequestOtp:      return "/payslip/request-otp"
        case .payslipVerifyOtp:       return "/payslip/verify-otp"
        case .payslipFetch:           return "/payslip/fetch"
            
        case .getUserBusinessCardState: return "/getUserBusinessCardState"
        case .activateDigitalCard:      return "/activateDigitalCard"
        case .deactivateDigitalCard:      return "/deactivateDigitalCard"
        case .menuIncrement: return "menuIncrement"
        }
    }
    
    var sampleData: Data {
        switch self {
        case .getFoodMenu: return Data.jsonFromBundle("yemek")
        default: return Data()
        }
    }
    
    var task: Task {
        switch self {
        case .checkPhone(let phoneNumber):
            return .requestParameters(parameters: ["phoneNumber":phoneNumber], encoding: JSONEncoding.default)
        case .login(let phoneNumber, let pin):
            return .requestParameters(parameters: ["phoneNumber":phoneNumber, "pin":pin], encoding: JSONEncoding.default)
        case .getNewsList(let type, let discountType, let locId, let pageNumber):
            if let discountType = discountType {
                return .requestParameters(parameters: ["type":type.rawValue,"discountType":discountType.rawValue ,"pageNumber":pageNumber], encoding: JSONEncoding.default)
            } else if let locId = locId {
                return .requestParameters(parameters: ["type":type.rawValue, "locId":locId, "pageNumber":pageNumber], encoding: JSONEncoding.default)
            }else {
                return .requestParameters(parameters: ["type":type.rawValue, "pageNumber":pageNumber], encoding: JSONEncoding.default)
            }
        case .getShuttleList(let type, let companyLocationId):
            return .requestParameters(parameters: ["type":type,"companyLocationId":companyLocationId], encoding: JSONEncoding.default)
        case .changeNotificationSetting(let type, let value):
            return .requestParameters(parameters: ["type":type,"value":value], encoding: JSONEncoding.default)
        case .saveDeviceInfo(let deviceToken), .deleteDeviceInfo(let deviceToken):
            return .requestParameters(parameters: ["osType":2,"deviceToken":deviceToken, "newApp":true], encoding: JSONEncoding.default)
        case .getNotificationList(let firstDate, let lastDate):
            var params : [String : Any] = [:]
            if let firstDate = firstDate { params["firstDate"] = firstDate }
            if let lastDate = lastDate { params["lastDate"] = lastDate }
            return .requestParameters(parameters: params, encoding: JSONEncoding.default)
        case .deleteNotification(let notificationId):
            return .requestParameters(parameters: ["notificationId":notificationId], encoding: JSONEncoding.default)
        case .initCall(let versionNumber):
            return .requestParameters(parameters: ["version":versionNumber, "osType":"2", "newApp":true], encoding: JSONEncoding.default)
        case .sendQRCode(let code):
            return .requestParameters(parameters: ["code":code], encoding: JSONEncoding.default)
        case .checkPhone(let phoneNumber):
            return .requestParameters(parameters: ["phoneNumber":phoneNumber], encoding: JSONEncoding.default)
        case .submitFeedback(let text):
            return .requestParameters(parameters: ["text":text], encoding: JSONEncoding.default)
   
        case .payslipRequestOtp:
            return .requestPlain
        case .payslipVerifyOtp(let code):
            return .requestParameters(parameters: ["otp": code], encoding: JSONEncoding.default)
        case .payslipFetch(let year, let month):
            return .requestParameters(parameters: ["year": year, "month": month], encoding: JSONEncoding.default)

        case .menuIncrement(let keyName):
            return .requestParameters(parameters: ["menu_key": keyName], encoding: JSONEncoding.default)
        default:
            return .requestPlain
        }
    }
    
    var headers: [String : String]? {
        if needAuthorization {
            
            return ["Authorization" : "Bearer \(TokenManager.sharedManager.accessToken)", "lang" : MBTConstants.Device.PreferredLanguageCode]
        }
        return ["lang" : MBTConstants.Device.PreferredLanguageCode]
    }
    
    var method: Moya.Method {
        switch self {
        case .getNewsDetail(_),.getDiscountCode(_),.getBirthdayList,.getFoodMenu,.getLocations,.getProfile,.getYearlyWorkHours(_),
                .getMonthlyWorkHours(_),.getWorkCalendar(_),.getShuttleOptions,.getNotificationSettings,.getNotificationBadgeCount,.signOut, .getCaptcha, .getUserConfig, .getClubs(_), .getClubLocs, .getMedias,
                .getPhones(_),.getPhoneLocs, .activateDigitalCard, .deactivateDigitalCard, .getUserBusinessCardState:
            return .get
        default:
            return .post
        }
    }
}

extension Data {
    
    static func jsonFromBundle(_ bundleName: String) -> Data {
        
        if let path = Bundle.main.path(forResource: bundleName, ofType: "json") {
            do {
                let data = try Data(contentsOf: URL(fileURLWithPath: path), options: .mappedIfSafe)
                return data
            } catch {
                return Data()
            }
        }
        return Data()
    }
}
