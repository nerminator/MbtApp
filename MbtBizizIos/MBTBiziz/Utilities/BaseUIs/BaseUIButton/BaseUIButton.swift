//
//  BaseUIButton.swift
//  MBT
//
//  Created by Serkut Yegin on 6.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class BaseUIButton: UIButton {

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
        titleLabel?.font = UIFont.init(name: type.fontName, size: titleLabel?.font.pointSize ?? 12)
    }
    
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        setCustomFont(with: fontType)
    }
    
    override func titleRect(forContentRect contentRect: CGRect) -> CGRect {
        var frame = super.titleRect(forContentRect: contentRect)
        frame.origin = CGPoint(x: frame.origin.x, y: frame.origin.y - 3)
        frame.size = CGSize(width: frame.size.width, height: frame.size.height + 6)
        return frame
    }

}
