//
//  MBTHomeCardView.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol MBTHomeCardViewDelegate : class {
    func homeCardViewDidSelected(_ homeCard : MBTHomeCardView)
}

class MBTHomeCardView: UIView {
    
    var backImageName:String? = "";
    @IBInspectable var icon:UIImage? {
        set {
            imgIcon.image = newValue
        }
        get {
            return imgIcon.image
        }
    }
    
    @IBInspectable var img:String? {
        
        set {
            backImageName = newValue;
            button.setBackgroundImage(UIImage(named: backImageName!), for: .normal);
        }
        get {
            return backImageName;
        }
    }
    
    @IBInspectable var title:String? {
        set {
            lblTitle.text = newValue?.localized()
        }
        get {
            return lblTitle.text
        }
    }
    
    @IBOutlet weak var button: UIButton!
    @IBOutlet weak var delegate : MBTHomeCardViewDelegate?
    @IBOutlet weak var imgIcon : UIImageView!
    @IBOutlet weak var lblTitle : BaseUILabelDemi!
    @IBOutlet weak var view: UIView!
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        Bundle.main.loadNibNamed(self.className, owner: self, options: nil)
        self.addSubview(view)
        view.autoPinEdgesToSuperviewEdges()
    }
    
    @IBAction func btnTapped(_ sender : UIButton) {
        delegate?.homeCardViewDidSelected(self)
    }

}
