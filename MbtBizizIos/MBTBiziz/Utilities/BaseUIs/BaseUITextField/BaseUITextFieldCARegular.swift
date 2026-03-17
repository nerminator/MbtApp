//
//  BaseUITextFieldCARegular.swift
//  MBT
//
//  Created by Serkut Yegin on 6.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class BaseUITextFieldCARegular: BaseUITextField {

    override init(frame: CGRect) {
        super.init(frame: frame)
        setCustomFont(with: .caRegular)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setCustomFont(with: .caRegular)
    }

}
