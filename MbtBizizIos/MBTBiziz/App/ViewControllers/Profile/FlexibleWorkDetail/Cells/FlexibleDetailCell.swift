//
//  FlexibleDetailCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 20.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class FlexibleDetailCell: UITableViewCell {

    @IBOutlet weak var lblDateTitle: BaseUILabelDemi!
    @IBOutlet weak var lblWorkDetails: BaseUILabelRegular!
    @IBOutlet weak var lblTotal: BaseUILabelLight!
    @IBOutlet weak var lblPlusKey: BaseUILabelRegular!
    @IBOutlet weak var lblPlusValue: BaseUILabelRegular!
    @IBOutlet weak var lblMinusKey: BaseUILabelRegular!
    @IBOutlet weak var lblMinusValue: BaseUILabelRegular!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ index: Int, workDetails:MBTFlexibleMonthlyDayList?, plusKey: String?, minusKey: String?) {
        contentView.backgroundColor = (index % 2 == 0) ? UIColor.white.withAlphaComponent(0.03) : UIColor.clear
        lblDateTitle.text = workDetails?.dayText
        lblWorkDetails.text = workDetails?.hoursText
        lblTotal.text = workDetails?.totalHours
        lblPlusValue.text = workDetails?.plusHours
        lblMinusValue.text = workDetails?.minusHours
        lblPlusKey.text = (plusKey?.uppercased(with: .mbtLocale) ?? "") + ":"
        lblMinusKey.text = (minusKey?.uppercased(with: .mbtLocale) ?? "") + ":"
    }

}
