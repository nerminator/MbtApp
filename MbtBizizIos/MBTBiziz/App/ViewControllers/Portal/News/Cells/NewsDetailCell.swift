//
//  NewsDetailCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class NewsDetailCell: UITableViewCell {

    @IBOutlet weak var lblTitle: BaseUILabelRegular!
    @IBOutlet weak var lblContent: BaseUILabelRegular!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ item : NewsDetailItem?, index:Int) {
        lblTitle.text = item?.title
        lblContent.text = item?.content
        if index == 0 {
            lblTitle.font = UIFont.mbtRegular(22)
            lblTitle.fontType = .regular
        } else {
            lblTitle.font = UIFont.mbtCARegular(18)
            lblTitle.fontType = .caRegular
        }
    }

}
