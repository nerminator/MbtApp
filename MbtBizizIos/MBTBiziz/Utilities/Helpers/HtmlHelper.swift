//
//  HtmlHelper.swift
//  MBT
//
//  Created by Serkut Yegin on 3.07.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

enum HtmlType : String {
    case appDescription = "APP_DESCRIPTION"
    case termOfUse = "TERMSOFUSE"
    case dataProtection = "DATAPROTECTION"
    case foss = "FOSS"
    case thirParty = "THIRDPARTY"
    case legal = "LEGAL"
    case appSupport = "APPSUPPORT"
    
    var title : String {
        switch self {
        case .appDescription: return "TXT_ABOUT_MAIN_APP_DESCRIPTION".localized()
        case .termOfUse: return "TXT_ABOUT_MAIN_TERMS_OF_USE".localized()
        case .dataProtection: return "TXT_ABOUT_MAIN_DATA_PROTECTION".localized()
        case .foss: return "TXT_ABOUT_MAIN_FOSS".localized()
        case .thirParty: return "TXT_ABOUT_MAIN_3RD_PARTY_CONTENT".localized()
        case .legal: return "TXT_ABOUT_MAIN_LEGAL_NOTICES".localized()
        case .appSupport: return "TXT_ABOUT_MAIN_APP_SUPPORT".localized()
        }
    }
    
    private var htmlPath : String {
        switch self {
        case .appDescription: return "appdescription"
        case .termOfUse: return "termsofuse"
        case .dataProtection: return "dataprotection"
        case .foss: return "foss"
        case .thirParty: return "thirdparty"
        case .legal: return "legal"
        case .appSupport: return "appsupport"
        }
    }
    
    var htmlString : String? {
        let htmlExtension = MBTLanguageManager.sharedManager.languageType.htmlExtension
        let htmlFile = Bundle.main.path(forResource: htmlPath + htmlExtension, ofType: "html")
        return try? String(contentsOfFile: htmlFile!, encoding: .utf8)
    }
}
