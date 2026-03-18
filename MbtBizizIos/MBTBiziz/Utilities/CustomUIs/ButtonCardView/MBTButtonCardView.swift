//
//  MBTButtonCardView.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

@objc protocol MBTButtonCardViewDelegate : class {
    func buttonCardViewDidSelected(_ buttonCard : MBTButtonCardView)
}

class MBTButtonCardView: UIView {
    
    var data = 0;
    
    @IBInspectable var title:String? {
        set {
            lblTitle.text = newValue?.localized()
        }
        get {
            return lblTitle.text
        }
    }

    
    @IBOutlet weak var button: UIButton!
    @IBOutlet weak var delegate : MBTButtonCardViewDelegate?
    @IBOutlet weak var lblTitle : BaseUILabelDemi!
    @IBOutlet weak var view: UIView!
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        Bundle.main.loadNibNamed(self.className, owner: self, options: nil)
        self.addSubview(view)
        view.autoPinEdgesToSuperviewEdges()
    }
    override init(frame: CGRect) {
        super.init(frame: frame)
        Bundle.main.loadNibNamed(self.className, owner: self, options: nil)
        self.addSubview(view)
        view.autoPinEdgesToSuperviewEdges()
    }
    
    @IBAction func btnTapped(_ sender : UIButton) {
        delegate?.buttonCardViewDidSelected(self)
    }

}
