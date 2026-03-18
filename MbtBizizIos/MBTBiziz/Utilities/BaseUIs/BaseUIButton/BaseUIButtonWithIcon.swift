//
//  BaseUIButtonWithIcon.swift
//  MBT
//
//  Created by Serkut Yegin on 6.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class BaseUIButtonWithIcon: UIButton {

    var label: UILabel!
    var image: UIImageView!
    
    var fontType : FontType = .regular {
        didSet {
            if fontType != oldValue {
                commonInit(with: fontType)
            }
        }
    }
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        commonInit(with: .regular)
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        commonInit(with: .regular)
    }
    
    func commonInit(with type:FontType) {
        fontType = type
        titleLabel?.font = UIFont.init(name: type.fontName, size: titleLabel?.font.pointSize ?? 12)
        imageEdgeInsets = UIEdgeInsets(top: 35, left: 0, bottom: 35, right: self.frame.width - 10)
        imageView!.contentMode = .scaleAspectFit;
        titleLabel?.numberOfLines = 2
        /*label = UILabel(frame: CGRect(x: 20, y: 20, width: 100, height: frame.height-40))
        image = UIImageView(frame: CGRect(x: frame.width-20-image.frame.width, y: 20, width: 50, height: frame.height-40))
                
        addSubview(label)
        addSubview(image)*/
    }
    
    override func traitCollectionDidChange(_ previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        commonInit(with: fontType)
    }
    
    /*override func titleRect(forContentRect contentRect: CGRect) -> CGRect {
        var frame = super.titleRect(forContentRect: contentRect)
        frame.origin = CGPoint(x: frame.origin.x, y: frame.origin.y - 3)
        frame.size = CGSize(width: frame.size.width, height: frame.size.height + 6)
        return frame
    }*/

}
