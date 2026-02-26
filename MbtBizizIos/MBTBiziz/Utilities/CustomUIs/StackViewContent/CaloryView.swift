//
//  CaloryView.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 12.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

extension Array where Element == CaloryView {
    
    func clearAll() {
        forEach({ $0.clear() })
    }
}

class CaloryView: UIView {

    @IBOutlet weak var lblTitle: BaseUILabelDemi!
    @IBOutlet weak var lblSubtitle: BaseUILabel!
    
    @discardableResult
    func setup(_ object:MBTFoodMenuCalorieDetails?) -> CaloryView {
        lblTitle.text = object?.name
        lblSubtitle.text = "\(object?.value ?? 0)"
        return self
    }
    
    func clear() {
        lblTitle.text = ""
        lblSubtitle.text = ""
    }

}
