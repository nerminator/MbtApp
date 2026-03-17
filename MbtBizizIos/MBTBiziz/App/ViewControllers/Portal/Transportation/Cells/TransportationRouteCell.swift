//
//  TransportationRouteCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 14.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class TransportationRouteCell: UITableViewCell {

    @IBOutlet weak var viewDot: UIView!
    @IBOutlet weak var viewLine: UIView!
    @IBOutlet weak var lblTitle: BaseUILabelRegular!
    @IBOutlet weak var imgViewDestination: UIImageView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ title:String, isSelected:Bool ,isLast:Bool) {
        lblTitle.text = title
        
        lblTitle.textColor = UIColor.white.withAlphaComponent(0.5)
        lblTitle.setCustomFont(with: .regular)
        viewDot.isHidden = false
        viewLine.isHidden = false
        imgViewDestination.isHidden = true
        viewDot.backgroundColor = UIColor.white.withAlphaComponent(0.3)
        imgViewDestination.tintColor = UIColor.clear
        
        
        if isSelected {
            viewDot.backgroundColor = UIColor.white
            lblTitle.textColor = UIColor.white
            lblTitle.setCustomFont(with: .demi)
            imgViewDestination.tintColor = UIColor.white
        }
        
        if isLast {
            viewDot.isHidden = true
            viewLine.isHidden = true
            imgViewDestination.isHidden = false
        }
    }

}
