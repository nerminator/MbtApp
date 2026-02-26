//
//  BaseUILabelDemi.swift
//  MBT
//
//  Created by Serkut Yegin on 5.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class BaseUILabelDemi: BaseUILabel {

    override init(frame: CGRect) {
        super.init(frame: frame)
        setCustomFont(with: .demi)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setCustomFont(with: .demi)
    }

}
