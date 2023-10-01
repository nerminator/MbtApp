//
//  VBTConstants.swift
//  MBT
//
//  Created by Serkut Yegin on 1.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

typealias VoidClosure = () -> Void

struct MBTConstants {
    
    struct Device {
        
        static let PreferredLanguage = Locale.preferredLanguages[0]
        static let LanguageDict = Locale.components(fromIdentifier:Device.PreferredLanguage)
        static let PreferredLanguageCode = Device.LanguageDict["kCFLocaleLanguageCodeKey"] == "tr" ? "tr" : "en"
        
        static var Width : CGFloat {
            return UIApplication.shared.keyWindow!.w
        }
        static var Height : CGFloat {
            return UIApplication.shared.keyWindow!.h
        }
        static var WidthTrimmed : CGFloat {
            return min(650, Width)
        }
        static let Idiom = UIDevice.current.userInterfaceIdiom
        static let releaseVersionNumber = Bundle.main.releaseVersionNumber
        static let buildVersionNumber = Bundle.main.buildVersionNumber
        static var isIpad : Bool {
            return self.Idiom == .pad
        }
        
        static var isTurkish : Bool {
            if PreferredLanguageCode == "tr" {
                return true
            }
            return false
        }
    }
    
    struct Constants {
        static let PhoneNumberTechnicalSupport = "00800142030248"
        static let CommentCharacterLength = 200
        static let Months : [String] = ["TXT_COMMON_JANUARY".localized(),
                                        "TXT_COMMON_FEBRUARY".localized(),
                                        "TXT_COMMON_MARCH".localized(),
                                        "TXT_COMMON_APRIL".localized(),
                                        "TXT_COMMON_MAY".localized(),
                                        "TXT_COMMON_JUNE".localized(),
                                        "TXT_COMMON_JULY".localized(),
                                        "TXT_COMMON_AUGUST".localized(),
                                        "TXT_COMMON_SEPTEMBER".localized(),
                                        "TXT_COMMON_OCTOBER".localized(),
                                        "TXT_COMMON_NOVEMBER".localized(),
                                        "TXT_COMMON_DECEMBER".localized()]
    }
    
    struct UserDefined {
        static let NewsTypeKey = "NewsTypeKey"
        static let FoodListKey = "FoodListKey"
        static let ShuttleListKey = "ShuttleListKey"
        static let DensityListKey = "DensityListKey"
        static let DensityBottomInset = "DensityBottomInset"
    }
    
    struct UserPreference {
        static let UserId = "MBTCONSTANTS_USERNAME"
        static let Token = "MBTCONSTANTS_TOKEN"
        static let PasswordNeedsChange = "MBTCONSTANTS_PASSWORD_NEEDS_CHANGE"
        static let TermsOfUseShown = "MBTCONSTANTS_TERMS_OF_USE_SHOWN"
        static let CalendarIdentifier = "MBTCONSTANTS_CALENDAR_IDENTIFIER"
        static let OneSignalDeleteTagError = "MBTCONSTANTS_ONESIGNAL_DELETETAG_ERROR"
        static let TransportationSearchIdentifier = "MBTCONSTANTS_TRANSPORTATION_SEARCH_IDENTIFIER"
        
        static func eventIdentifier(_ eventId : Int) -> String {
            return "MBT_CALENDAR_EVENT_\(eventId)"
        }
    }
}

extension Bundle {
    
    var releaseVersionNumber: String? {
        return infoDictionary?["CFBundleShortVersionString"] as? String
    }
    var buildVersionNumber: String? {
        return infoDictionary?["CFBundleVersion"] as? String
    }
}
