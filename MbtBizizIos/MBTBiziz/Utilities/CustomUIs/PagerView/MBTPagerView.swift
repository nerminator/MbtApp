//
//  MBTPagerView.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 29.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class MBTPagerView: UIView {

    @IBOutlet weak var view: UIView!
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        Bundle.main.loadNibNamed(self.className, owner: self, options: nil)
        self.addSubview(view)
        view.autoPinEdgesToSuperviewEdges()
    }

}
