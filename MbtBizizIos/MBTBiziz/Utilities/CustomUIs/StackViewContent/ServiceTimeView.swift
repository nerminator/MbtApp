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
        lblTimeValue.text = object?.timeText
        return self
    }

}
