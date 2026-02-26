//
//  MealMenuHeaderCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 12.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class MealMenuHeaderCell: UITableViewCell {

    @IBOutlet weak var lblDate: BaseUILabelDemi!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ date:String?, isToday:Bool?) {
        guard let date = date else { lblDate.text = ""; return }
        let safeToday = isToday ?? false
        lblDate.text = safeToday ? (date + " (\("TXT_COMMON_TODAY".localized()))") : date
    }

}
