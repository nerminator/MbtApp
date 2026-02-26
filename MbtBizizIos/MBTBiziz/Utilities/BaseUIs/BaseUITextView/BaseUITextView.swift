//
//  BaseUITextView.swift
//  MBT
//
//  Created by Serkut Yegin on 11.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class BaseUITextView: UITextView {

    var fontType : FontType = .regular {
        didSet {
            if fontType != oldValue {
                setCustomFont(with: fontType)
            }
        }
    }
    
    override init(frame: CGRect, textContainer: NSTextContainer?) {
        super.init(frame: frame, textContainer: textContainer)
        setCustomFont(with: .regular)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        setCustomFont(with: .regular)
    }
    
    func setCustomFont(with type:FontType) {
        fontType = type
        font = UIFont.init(name: type.fontName, size: font?.pointSize ?? 12)
    }
    
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        setCustomFont(with: fontType)
    }

}
