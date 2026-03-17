//
//  NewsSubtitleCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class NewsSubtitleCell: UITableViewCell {

    @IBOutlet weak var lblSubtitle: BaseUILabelRegular!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ listItem : MBTNewsNewsList?) {
        lblSubtitle.text = listItem?.text
    }

}
