//
//  NewsContactCell.swift
//  MBTBiziz
//
//  Created by Nermy on 1.03.2020.
//  Copyright © 2020 Serkut Yegin. All rights reserved.
//

import UIKit

class NewsContactCell: UITableViewCell {
    
    @IBOutlet weak var imgView: UIImageView!
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
        imgView.mbtSetImage(urlStr: listItem?.image, placeHolder: #imageLiteral(resourceName: "imagePlaceholder"))
    }
    
}
