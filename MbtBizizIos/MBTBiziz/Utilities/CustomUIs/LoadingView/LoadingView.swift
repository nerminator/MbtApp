//
//  LoadingView.swift
//

import UIKit

class LoadingView: UIView {

    @IBOutlet weak var visualEffectView: UIVisualEffectView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        visualEffectView.roundCorners(45)
    }
    var isOnFire : Bool = false {
        willSet {
            if newValue == isOnFire { return }
            if newValue {
                guard let window = UIApplication.shared.keyWindow else { return }
                window.addSubview(self)
                window.bringSubview(toFront: self)
                autoPinEdgesToSuperviewEdges()
                activityIndicator.startAnimating()
                window.endEditing(true)
            } else {
                activityIndicator.stopAnimating()
                removeFromSuperview()
            }
        }
    }
    
}
