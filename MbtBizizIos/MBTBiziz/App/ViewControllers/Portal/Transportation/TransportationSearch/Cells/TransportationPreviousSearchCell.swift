//
//  TransportationPreviousSearchCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 12.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class TransportationPreviousSearchCell: UITableViewCell {

    @IBOutlet weak var lblPreviousSearch: BaseUILabelRegular!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ searchItem : MBTTransportationSearchItem?) {
        lblPreviousSearch.text = searchItem?.title
    }

}
