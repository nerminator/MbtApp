//
//  RoundedImageView.swift
//  MBT
//
//  Created by Serkut Yegin on 6.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class RoundedImageView: UIImageView {

    override func awakeFromNib() {
        super.awakeFromNib()
        self.clipsToBounds = true
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        self.roundView()
    }

}
