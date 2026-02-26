//
//  MealMenuCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 12.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class MealMenuCell: UITableViewCell {

    @IBOutlet weak var imgViewArrow: UIImageView!
    @IBOutlet weak var lblMeal: BaseUILabelDemi!
    @IBOutlet weak var lblCalori: BaseUILabelLight!
    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var lblInfo: BaseUILabel!
    fileprivate var caloryViews : [CaloryView] = []
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        for _ in 0..<3 {
            let caloryView = CaloryView.fromNib()
            stackView.addArrangedSubview(caloryView)
            self.caloryViews.append(caloryView)
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(_ object: MBTFoodMenuFoodList?, index:Int, isExpanded:Bool) {
        contentView.backgroundColor = index % 2 == 0 ? UIColor.white.withAlphaComponent(0.03) : UIColor.clear
        setExpanded(isExpanded)
        lblMeal.text = object?.name
        lblCalori.text = "\(object?.calorie ?? 0)"
        lblInfo.text = object?.info
        
        caloryViews.clearAll()
        object?.calorieDetails?.forEachEnumerated({ (index,detail) in
            if index < self.caloryViews.count {
                self.caloryViews[index].setup(detail)
            }
        })
    }
    
    func setExpanded(_ expanded:Bool, animated:Bool = false) {
        if animated {
            UIView.animate(withDuration: 0.2) {
                self.imgViewArrow.transform = expanded ? CGAffineTransform(rotationAngle: .pi) : .identity
            }
        } else {
            imgViewArrow.transform = expanded ? CGAffineTransform(rotationAngle: .pi) : .identity
        }
    }
    
}
