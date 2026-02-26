//
//  NotificationTableCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 31.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class NotificationTableCell: UITableViewCell {

    @IBOutlet weak var viewUnreadMark: UIView!
    @IBOutlet weak var lblDescription: BaseUILabelRegular!
    @IBOutlet weak var lblTitle: BaseUILabelDemi!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        lblTitle.text = ""
        lblDescription.text = ""
    }
    
    func setup(_ notification : MBTNotificationListItem?) {
        lblDescription.text = notification?.listText
        if let type = notification?.typeEnum {
            if type == .indirim {
                if let discountType = notification?.discountTypeEnum {
                    lblTitle.text = type.title + " - " + discountType.title
                } else {
                    lblTitle.text = type.title
                }
            } else {
                lblTitle.text = type.title
            }
        }
        
        if notification?.isSeen ?? true {
            viewUnreadMark.isHidden = true
            contentView.backgroundColor = UIColor.clear
        } else {
            viewUnreadMark.isHidden = false
            contentView.backgroundColor = UIColor.white.withAlphaComponent(0.03)
        }
    }

}
