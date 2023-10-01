//
//  GradientView.swift
//  MBT
//
//  Created by Serkut Yegin on 13.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class GradientView: UIView {

    fileprivate var gradientLayer : CAGradientLayer?
    
    @IBInspectable var topColor : UIColor = .black {
        didSet {
            addGradientLayer()
        }
    }
    
    @IBInspectable var bottomColor : UIColor = .black {
        didSet {
            addGradientLayer()
        }
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        addGradientLayer()
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        gradientLayer?.frame = bounds
    }
    
    func addGradientLayer() {
        
        if let gradientLayer = gradientLayer {
            gradientLayer.removeFromSuperlayer()
        }
               
        let colorDark = UIColor(hexString: "#000000")!.cgColor
        let color30Percent =  UIColor(hexString: "#2e2e2e")!.cgColor
        let height = bounds.height
        
        let b1 = NSNumber( value: Float(1000/height))
        let b2 = NSNumber( value: Float(500/height))
        var bottomDarkStartLocation : NSNumber = height > 1000 ? b1 : b2
        if (bottomDarkStartLocation.floatValue > 1){
            bottomDarkStartLocation = 1
        }
        
        let lightStartLocation = NSNumber(value: Float(bottomDarkStartLocation.floatValue * 0.3))
        
        gradientLayer = CAGradientLayer()
        gradientLayer!.colors = [ colorDark, color30Percent, colorDark ]
        gradientLayer!.locations = [ 0.0, lightStartLocation, bottomDarkStartLocation]
        gradientLayer!.frame = bounds
        layer.insertSublayer(gradientLayer!, at: 0)
        
    }
}
