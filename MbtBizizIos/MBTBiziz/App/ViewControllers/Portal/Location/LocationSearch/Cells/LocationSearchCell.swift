//
//  LocationSearchCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class LocationSearchCell: UITableViewCell {

    @IBOutlet weak var lblRoomName: BaseUILabelDemi!
    @IBOutlet weak var lblBuildingName: BaseUILabelRegular!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ roomModel : MBTLocationMeetingRoomList?, index:Int) {
        lblRoomName.text = roomModel?.name
        lblBuildingName.text = roomModel?.buildingName
        contentView.backgroundColor = (index % 2 == 0) ? UIColor.white.withAlphaComponent(0.03) : UIColor.clear
    }

}
