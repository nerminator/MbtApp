//
//  AboutDescriptionCell.swift
//  MBT
//
//  Created by Serkut Yegin on 24.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class AboutDescriptionCell: UITableViewCell {
    @IBOutlet weak var aboutTextView: BaseUITextView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
