//
//  MealMenuServiceCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 12.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class MealMenuServiceCell: UITableViewCell {

    @IBOutlet weak var lblCity: BaseUILabelDemi!
    @IBOutlet weak var stackView: UIStackView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ object: MBTFoodMenuShuttleList?) {
        lblCity.text = object?.location
        stackView.clearSubviews()
        object?.timeList?.forEach({ (timeList) in
            self.stackView.addArrangedSubview(ServiceTimeView.fromNib().setup(timeList))
        })
    }

}
