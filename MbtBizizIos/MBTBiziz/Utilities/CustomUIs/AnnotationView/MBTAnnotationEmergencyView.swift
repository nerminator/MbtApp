//
//  MBTAnnotationView.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 5.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import MapKit

class MBTAnnotationEmergencyView: UIView {

    @IBOutlet weak var viewSelectedBgColor: UIView!
    @IBOutlet weak var viewBgColor: UIView!
    @IBOutlet weak var imgEmergency: UIImageView!
    @IBOutlet weak var lblSubtitle: BaseUILabelDemi!
    
    fileprivate var isSelected : Bool = false
    
    override func awakeFromNib() {
        super.awakeFromNib()
        //imgEmergency.roundCorners(22)
    }
    
    func setup(_ number:String?, selected:Bool) {
        lblSubtitle.text = number;
        setSelected(selected, animated: false)
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

class EmergencyAnnotationView : MKAnnotationView {
    
    fileprivate let mbtAnnotationView = MBTAnnotationEmergencyView.fromNib()
    
    override var annotation: MKAnnotation? {
        didSet {
            guard let annotation = annotation as? MBTLocationBuildingList else { return }
            mbtAnnotationView.setup(annotation.shortName, selected: isSelected)
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
        mbtAnnotationView.frame = CGRect.init(x: 0, y: 0, w: 32, h: 40)
        self.addSubview(mbtAnnotationView)
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        mbtAnnotationView.setSelected(selected, animated: animated)
    }
    
}
