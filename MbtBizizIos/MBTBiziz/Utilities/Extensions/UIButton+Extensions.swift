//
//  UIButton+Extensions.swift
//  MBT
//
//  Created by Serkut Yegin on 20.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

extension UIButton {

    func setTitleWithoutAnimation(title:String?) {
        UIView.performWithoutAnimation {
            self.setTitle(title, for: .normal)
            self.setTitle(title, for: .highlighted)
            self.setTitle(title, for: .disabled)
            self.setTitle(title, for: .selected)
            self.layoutIfNeeded()
        }
    }
}
