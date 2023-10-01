//
//  AboutVersionCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 5.01.2019.
//  Copyright © 2019 Serkut Yegin. All rights reserved.
//

import UIKit

class AboutVersionCell: UITableViewCell {

    @IBOutlet weak var lblVersionText: BaseUILabelRegular!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        if let version = MBTConstants.Device.releaseVersionNumber {
            lblVersionText.text = "Version - \(version)"
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
