//
//  CommonExtensions.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

extension UITextField {
    
    open override func canPerformAction(_ action: Selector, withSender sender: Any?) -> Bool {
        return false
    }
}

extension Int {
    
    func toTimeStr() -> String {
        var minutes = "\(self/60)"
        var seconds = "\(self%60)"
        if minutes.length == 1 { minutes = "0" + minutes }
        if seconds.length == 1 { seconds = "0" + seconds }
        return minutes + ":" + seconds
    }
    
    var monthStr : String {
        guard self > 0 , self - 1 < MBTConstants.Constants.Months.count else { return "" }
        return MBTConstants.Constants.Months[self - 1]
    }
}

extension Locale {
    
    static let tr = Locale.init(identifier: "tr_TR")
    static let en = Locale.init(identifier: "en_US")
    static let mbtLocale : Locale = MBTConstants.Device.isTurkish ? .tr : .en
}

extension CGFloat {
    
    static var statusBarHeight : CGFloat {
        if #available(iOS 11.0, *) {
            let value = UIApplication.shared.keyWindow?.safeAreaInsets.top ?? 20 
            return Swift.max(value, 20)
        } else {
            return UIApplication.shared.statusBarFrame.height
        }
    }
    
    static var bottomSafeArea : CGFloat {
        if #available(iOS 11.0, *) {
            return UIApplication.shared.keyWindow?.safeAreaInsets.bottom ?? 0
        } else {
            return 0
        }
    }
}
