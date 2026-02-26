//
//  UIFont+Extensions.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 18.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

extension UIFont {
    
    class func mbtRegular(_ size : CGFloat) -> UIFont? {
        return UIFont(name: FontType.regular.fontName, size: size)
    }
    
    class func mbtLight(_ size : CGFloat) -> UIFont? {
        return UIFont(name: FontType.light.fontName, size: size)
    }
    
    class func mbtDemi(_ size : CGFloat) -> UIFont? {
        return UIFont(name: FontType.demi.fontName, size: size)
    }
    
    class func mbtCARegular(_ size : CGFloat) -> UIFont? {
        return UIFont(name: FontType.caRegular.fontName, size: size)
    }
}
