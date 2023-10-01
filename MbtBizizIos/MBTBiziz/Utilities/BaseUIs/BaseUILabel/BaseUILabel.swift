//
//  BaseUILabel.swift
//  MBT
//
//  Created by Serkut Yegin on 5.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

enum FontType {
    case regular, demi, light, caRegular
    
    var fontName : String {
        switch self {
        case .regular: return "DaimlerCS-Regular"
        case .demi: return "DaimlerCS-Demi"
        case .light: return "DaimlerCS-Light"
        case .caRegular: return "DaimlerCA-Regular"
        }
    }
}

class BaseUILabel: UILabel {

    var fontType : FontType = .regular {
        didSet {
            if fontType != oldValue {
                setCustomFont(with: fontType)
            }
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        setCustomFont(with: .regular)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setCustomFont(with: .regular)
    }
    
    func setCustomFont(with type:FontType) {
        fontType = type
        font = UIFont(name: type.fontName, size: font.pointSize)
        //adjustsFontSizeToFitWidth = true
        //minimumScaleFactor = 0.8
    }
    
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        setCustomFont(with: fontType)
    }
}
