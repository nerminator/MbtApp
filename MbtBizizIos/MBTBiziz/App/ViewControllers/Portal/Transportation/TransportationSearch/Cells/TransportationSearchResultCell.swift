//
//  TransportationSearchResultCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 12.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class TransportationSearchResultCell: UITableViewCell {

    @IBOutlet weak var lblProvince: BaseUILabelRegular!
    @IBOutlet weak var lblSearchPlace: BaseUILabelDemi!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ stopListItem : MBTTransportationListStopList?, index:Int) {
        contentView.backgroundColor = (index % 2 == 0) ? UIColor.white.withAlphaComponent(0.03) : UIColor.clear
        lblSearchPlace.text = stopListItem?.name
        lblProvince.text = stopListItem?.shuttleName
    }

}
