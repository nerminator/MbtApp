//
//  BirthdayTableCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class BirthdayTableCell: UITableViewCell {

    @IBOutlet weak var lblPerson: BaseUILabelRegular!
    @IBOutlet weak var lblDuty: BaseUILabelRegular!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ listItem: MBTBirthdayBirthdayList?, index:Int) {
        lblPerson.text = listItem?.name
        lblDuty.text = listItem?.title
        contentView.backgroundColor = (index % 2 == 0) ? UIColor.clear : UIColor.init(r: 244, g: 244, b: 244).withAlphaComponent(0.03)
    }

}
