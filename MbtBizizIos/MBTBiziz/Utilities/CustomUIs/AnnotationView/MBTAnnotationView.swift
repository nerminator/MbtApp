//
//  MBTAnnotationView.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 5.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import MapKit

class MBTAnnotationView: UIView {

    @IBOutlet weak var viewSelectedBgColor: UIView!
    @IBOutlet weak var viewBgColor: RoundedView!
    @IBOutlet weak var lblNumber: BaseUILabelDemi!
    
    fileprivate var isSelected : Bool = false
    
    override func awakeFromNib() {
        super.awakeFromNib()
        addShadow(offset: CGSize.init(width: 4, height: 4), radius: 8, color: UIColor.mbtDark, opacity: 0.5)
        
    }
    
    func setup(_ number:String?, selected:Bool) {
        setSelected(selected, animated: false)
        lblNumber.text = number
    }
    
    func setSelected(_ selected: Bool, animated: Bool) {
        self.isSelected = selected
        if animated {
            UIView.animate(withDuration: 0.1) {
                self.viewSelectedBgColor.alpha = selected ? 1 : 0
            }
        } else {
            viewSelectedBgColor.alpha = selected ? 1 : 0
        }
    }
    
}

class CustomAnnotationView : MKAnnotationView {
    
    fileprivate let mbtAnnotationView = MBTAnnotationView.fromNib()
    
    override var annotation: MKAnnotation? {
        didSet {
            guard let annotation = annotation as? MBTLocationBuildingList else { return }
            mbtAnnotationView.setup(annotation.order?.toString, selected: isSelected)
        }
    }
    
    override init(annotation: MKAnnotation?, reuseIdentifier: String?) {
        super.init(annotation: annotation, reuseIdentifier: reuseIdentifier)
        addAnnotation()
        canShowCallout = false
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        addAnnotation()
        canShowCallout = false
    }
    
    fileprivate func addAnnotation() {
        mbtAnnotationView.frame = CGRect.init(x: 0, y: 0, w: 40, h: 40)
        self.addSubview(mbtAnnotationView)
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        mbtAnnotationView.setSelected(selected, animated: animated)
    }
    
}
