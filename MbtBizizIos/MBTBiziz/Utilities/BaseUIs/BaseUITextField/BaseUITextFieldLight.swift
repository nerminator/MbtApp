//
//  BaseUITextFieldLight.swift
//  MBT
//
//  Created by Serkut Yegin on 6.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class BaseUITextFieldLight: BaseUITextField {

    override init(frame: CGRect) {
        super.init(frame: frame)
        setCustomFont(with: .light)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setCustomFont(with: .light)
    }

}
