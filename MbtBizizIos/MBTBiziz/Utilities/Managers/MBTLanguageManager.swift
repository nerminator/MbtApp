//
//  MBTLanguageManager.swift
//  MBT
//
//  Created by Serkut Yegin on 1.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

enum LanguageType {
    case turkish
    case english
    
    var plistName : String {
        switch self {
        case .turkish: return "language_turkish"
        case .english: return "language_english"
        }
    }
    
    var htmlExtension : String {
        switch self {
        case .turkish: return "TR"
        case .english: return "EN"
        }
    }
}

class MBTLanguageManager: NSObject {
    
    @objc static let sharedManager = MBTLanguageManager()
    internal var languageDict : [String:String] = [:]
    internal var currentLanguage : String = ""
    internal var currentVersion : String = ""
    internal var languageType : LanguageType = .turkish
}

extension MBTLanguageManager {
    
    func initialize() {
        languageType = MBTConstants.Device.isTurkish ? .turkish : .english
        readFromSource(with: languageType)
    }
    
    private func readFromSource(with language:LanguageType) {
        DispatchQueue.global().async { [unowned self] in
            let root = NSDictionary(contentsOfFile: Bundle.main.path(forResource: "\(language.plistName)", ofType: "plist")!);
            if let dict = root?["language"] as? [String:String] {
                self.languageDict = dict
            }
        }
    }
    
    @objc public func getLocalizedString(key:String) -> String {
        
        var returnValue = key
        
        if let valueFromDict = self.languageDict[key] {
            if !valueFromDict.isEmpty {
                returnValue = valueFromDict
            }
        }
        return returnValue
    }
}

extension String {
    
    public func localized() -> String {
        return MBTLanguageManager.sharedManager.getLocalizedString(key: self)
    }
}
