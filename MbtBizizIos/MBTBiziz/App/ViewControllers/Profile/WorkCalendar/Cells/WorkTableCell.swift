//
//  WorkTableCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class WorkTableCell: UITableViewCell {

    @IBOutlet weak var viewColor: UIView!
    @IBOutlet weak var lblTitle: BaseUILabelDemi!
    @IBOutlet weak var lblDate: BaseUILabelDemi!
    @IBOutlet weak var lblToday: BaseUILabelRegular!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ index: Int, dayText:String?, typeListItem: MBTWorkCalendarTypeList?, isToday:Bool) {
        contentView.backgroundColor = (index%2 == 0) ? UIColor.white.withAlphaComponent(0.03) : UIColor.clear
        viewColor.backgroundColor = UIColor.fromHexString(typeListItem?.typeColor)
        lblTitle.text = typeListItem?.typeName
        lblDate.text = dayText
        lblToday.isHidden = !isToday
    }
    
    func setup(_ index: Int, dayText:String?, typeName: String?, typeColor: String?, isToday:Bool) {
        contentView.backgroundColor = (index%2 == 0) ? UIColor.white.withAlphaComponent(0.03) : UIColor.clear
        viewColor.backgroundColor = UIColor.fromHexString(typeColor)
        lblTitle.text = typeName
        lblDate.text = dayText
        lblToday.isHidden = !isToday
    }

}
