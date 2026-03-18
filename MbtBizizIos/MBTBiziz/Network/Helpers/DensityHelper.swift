//
//  DensityHelper.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class DensityHelper {

    fileprivate var densityInfo: [MBTFoodMenuDensityInfo]?
    fileprivate weak var containerController : UIViewController?
    fileprivate let densityView = DensityView.fromNib()
    fileprivate var constDensityHeight : NSLayoutConstraint?

    static var densityViewHeight : CGFloat {
        return CGFloat.bottomSafeArea + 90
    }
    
    @discardableResult
    func showDensityInfoIfNeccesary(_ fromVC : UIViewController?, densityInfo : [MBTFoodMenuDensityInfo]?) -> CGFloat {
        guard let fromVC = fromVC, let densityInfo = densityInfo, densityInfo.count > 0 else { return 0 }
        self.containerController = fromVC
        self.densityInfo = densityInfo
        
        densityView.setup(densityInfo)
        fromVC.view.addSubview(densityView)
        densityView.autoPinEdge(.leading, to: .leading, of: fromVC.view)
        densityView.autoPinEdge(.trailing, to: .trailing, of: fromVC.view)
        densityView.autoPinEdge(.bottom, to: .bottom, of: fromVC.view)
        constDensityHeight = densityView.autoSetDimension(.height, toSize: 0)
        fromVC.view.layoutIfNeeded()
        animate()
        return DensityHelper.densityViewHeight
    }
    
    fileprivate func animate() {
        constDensityHeight?.constant = DensityHelper.densityViewHeight
        UIView.animate(withDuration: 0.3, delay: 0.2, options: .curveEaseInOut , animations: {
            self.containerController?.view.layoutIfNeeded()
        }, completion: nil)
    }
    
}
