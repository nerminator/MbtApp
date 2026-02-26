//
//  ServiceTimeView.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 12.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class ServiceTimeView: UIView {

    @IBOutlet weak var lblTimeKey: BaseUILabel!
    @IBOutlet weak var lblTimeValue: BaseUILabel!
    
    @discardableResult
    func setup(_ object:MBTFoodMenuTimeList?) -> ServiceTimeView {
        lblTimeKey.text = object?.name
        if (lblTimeKey.text == "Akşam Yemeği" ||  lblTimeKey.text == "Dinner") {
            let baseText = lblTimeKey.text! + "*"
            let attributed = NSMutableAttributedString(string: baseText)
            // Superscript effect: move baseline up & shrink size
            attributed.addAttributes([
                .baselineOffset: 6,
                .font: lblTimeKey.font.withSize(lblTimeKey.font.pointSize * 0.7)
            ], range: NSRange(location: baseText.count - 1, length: 1))
            
            lblTimeKey.attributedText = attributed
        }
        lblTimeValue.text = object?.timeText
        return self
    }

}
