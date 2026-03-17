//
//  UIView+Extensions.swift
//
//  Created by Serkut Yegin.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

extension UIView {
    
    class var safeAreaBottomInset : CGFloat {
        guard let window = UIApplication.shared.keyWindow else { return 0 }
        if #available(iOS 11.0, *) {
            return window.safeAreaInsets.bottom
        } else {
            return 0
        }
    }
    
}

extension UIView : ModelType { }
extension ModelType where Self : UIView {
    static func fromNib(_ nibName : String = String(describing: Self.self)) -> Self {
        return Self.create(with: nibName) as! Self
    }
}

extension UIView {
    
    open class func create() -> UIView {
        return UIView.create(with: String(describing: self))
    }
    
    open class func create(with nibName:String) -> UIView {
        return Bundle.main.loadNibNamed(nibName, owner: nil, options: nil)?.first as! UIView
    }
    
}

extension UIView {
    
    func layoutIfNeededAnimated(_ duration : TimeInterval = 0.3) {
        UIView.animate(withDuration: duration) { [weak self] in
            self?.layoutIfNeeded()
        }
    }
    
    func clearSubviews() {
        if let stackView = self as? UIStackView {
            stackView.arrangedSubviews.forEach { [weak stackView] (view) in
                stackView?.removeArrangedSubview(view)
                view.removeFromSuperview()
            }
        } else {
            for view in subviews {
                view.removeFromSuperview()
            }
        }
    }
    
    public func roundCorners(_ radius: CGFloat, borderColor : UIColor? = nil, borderWidth: CGFloat? = nil) {
        layer.masksToBounds = true
        layer.cornerRadius = radius
        layer.borderWidth = borderWidth ?? 0
        layer.borderColor = borderColor?.cgColor ?? UIColor.clear.cgColor
    }
    
    public func addSubviewWithAutolayout(_ subview:UIView) {
        addSubview(subview)
        subview.autoPinEdgesToSuperviewEdges()
        layoutIfNeeded()
    }
    
    class func emptyView() -> UIView {
        let view = UIView()
        view.backgroundColor = UIColor.clear
        return view
    }
    
    public func showAnimated(duration:TimeInterval = 0.2, alpha: CGFloat = 1) {
        self.alpha = 0
        self.isHidden = false
        UIView.animate(withDuration: duration) { [unowned self] in
            self.alpha = alpha
        }
    }
    
    public func hideAnimated(duration:TimeInterval = 0.2, completion:(()->())? = nil) {
        self.alpha = 1
        UIView.animate(withDuration: duration, animations: { [unowned self] in
            self.alpha = 0
        }) { [unowned self] (finished) in
            if finished {
                self.isHidden = true
                completion?()
            }
        }
    }

    public func addQrCodeMaskLayer() {

        layer.sublayers?.forEach({ $0.removeFromSuperlayer() })

        let viewSize = bounds.size
        let xPosition = (viewSize.width - 300) / 2
        let yPosition = (viewSize.height - 300) / 2

        let path = UIBezierPath(roundedRect: CGRect(x: 0, y: 0, width: bounds.size.width, height: bounds.size.height), cornerRadius: 0)
        let squarePath = UIBezierPath(rect: CGRect(x: xPosition, y: yPosition, width: 300, height: 300))
        path.append(squarePath)
        path.usesEvenOddFillRule = true

        let fillLayer = CAShapeLayer()
        fillLayer.path = path.cgPath
        fillLayer.fillRule = kCAFillRuleEvenOdd
        fillLayer.fillColor = UIColor.mbtDark.cgColor
        fillLayer.opacity = 0.8
        layer.addSublayer(fillLayer)

        let borderLayer = CAShapeLayer()
        borderLayer.fillColor = UIColor.clear.cgColor
        borderLayer.path = UIBezierPath(rect: CGRect(x: xPosition, y: yPosition, width: 300, height: 300)).cgPath
        borderLayer.strokeColor = UIColor.white.cgColor
        layer.addSublayer(borderLayer)
    }
}

extension UIImage {
    
    convenience init(view: UIView) {
        UIGraphicsBeginImageContextWithOptions(view.bounds.size, view.isOpaque, 0.0)
        view.layer.render(in:UIGraphicsGetCurrentContext()!)
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        self.init(cgImage: image!.cgImage!)
    }
    
}
