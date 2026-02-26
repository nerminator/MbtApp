//
//  FlexibleTableCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 20.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class FlexibleTableCell: UITableViewCell {

    @IBOutlet weak var lblMonth: BaseUILabelDemi!
    @IBOutlet weak var lblTotal: BaseUILabelLight!
    @IBOutlet weak var lblPlus: BaseUILabelRegular!
    @IBOutlet weak var lblMinus: BaseUILabelRegular!
    @IBOutlet weak var lblPlusKey: BaseUILabelRegular!
    @IBOutlet weak var lblMinusKey: BaseUILabelRegular!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ item: MBTFlexibleYearlyMonthList?, plusKey: String?, minusKey: String?, index: Int) {
        contentView.backgroundColor = (index % 2 == 0) ? UIColor.white.withAlphaComponent(0.03) : UIColor.clear
        lblMonth.text = item?.month?.monthStr
        lblTotal.text = item?.totalHours
        lblPlus.text = item?.plusHours
        lblMinus.text = item?.minusHours
        lblPlusKey.text = (plusKey?.uppercased(with: .mbtLocale) ?? "") + ":"
        lblMinusKey.text = (minusKey?.uppercased(with: .mbtLocale) ?? "") + ":"
    }

}
