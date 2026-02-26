//
//  NewsImageCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class NewsImageCell: UITableViewCell {

    @IBOutlet weak var imgViewDiscount: UIImageView!
    @IBOutlet weak var lblSubtitle: BaseUILabelRegular!
    @IBOutlet weak var lblTitle: BaseUILabelDemi!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    func setup(_ listItem : MBTNewsNewsList?) {
        lblTitle.text = listItem?.discountTypeEnum?.title.uppercased(with: .mbtLocale)
        lblSubtitle.text = listItem?.text
        imgViewDiscount.mbtSetImage(urlStr: listItem?.image, placeHolder: #imageLiteral(resourceName: "imagePlaceholder"))
    }
    
}
