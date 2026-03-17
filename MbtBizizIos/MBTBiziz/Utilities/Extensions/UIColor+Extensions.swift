//
//  UIColor+Extensions.swift
//  MBT
//
//  Created by Serkut Yegin on 5.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

extension UIColor {

    open class var mbtBlue : UIColor {
        return UIColor.init(r: 0, g: 173, b: 239)
    }
    
    open class var mbtDarkBlue : UIColor {
        return UIColor.init(r: 43, g: 54, b: 78)
    }
    
    open class var mbtDark : UIColor {
        return UIColor.init(r: 29, g: 33, b: 42)
    }
    
    open class var mbtGray99 : UIColor {
        return UIColor.init(r: 153, g: 153, b: 153)
    }
    
    open class var mbtGrayLivePoll : UIColor {
        return UIColor.init(r: 142, g: 144, b: 148)
    }
    
    open class var mbtToolbarBg : UIColor {
        return UIColor.init(r: 57, g: 67, b: 84)
    }
    
    open class var mbtPickerBg : UIColor {
        return UIColor.init(r: 46, g: 53, b: 66)
    }
    
    class func fromHexString(_ hex:String?) -> UIColor {
        guard let hex = hex else { return UIColor.clear }
        
        var cString:String = hex.trimmingCharacters(in: .whitespacesAndNewlines).uppercased()
        
        if (cString.hasPrefix("#")) {
            cString.remove(at: cString.startIndex)
        }
        
        if ((cString.count) != 6) {
            return UIColor.clear
        }
        
        var rgbValue:UInt32 = 0
        Scanner(string: cString).scanHexInt32(&rgbValue)
        
        return UIColor(
            red: CGFloat((rgbValue & 0xFF0000) >> 16) / 255.0,
            green: CGFloat((rgbValue & 0x00FF00) >> 8) / 255.0,
            blue: CGFloat(rgbValue & 0x0000FF) / 255.0,
            alpha: CGFloat(1.0)
        )
    }
}
