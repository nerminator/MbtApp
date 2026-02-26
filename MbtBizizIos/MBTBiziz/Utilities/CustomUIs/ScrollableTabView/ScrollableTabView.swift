//
//  ScrollableTabView.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 29.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum TabDistrubition {
    case equal(spacing:CGFloat), proportionally
}

@objc protocol ScrollableTabViewDelegate : class {
    func scrollableTabView(_ scrollableTabView: ScrollableTabView, didSelectItemAt index:Int, isForward:Bool)
}

class ScrollableTabView: UIView {

    @IBOutlet weak var delegate : ScrollableTabViewDelegate?
    @IBOutlet weak var viewIndicator: UIView!
    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var scrollContentView: UIView!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var view: UIView!
    @IBOutlet weak var constIndicatorLeading: NSLayoutConstraint!
    @IBOutlet weak var constIndicatorWidth: NSLayoutConstraint!
    @IBOutlet weak var constContentWidth: NSLayoutConstraint!
    
    fileprivate var arrTabButtons : [UIButton] = []
    fileprivate var distrubition : TabDistrubition = .proportionally
    fileprivate var headers = true
    
    var currentPage = 0 {
        willSet {
            scrollContentIfNeccessary(currentPage, nextIndex: newValue)
        }
        didSet {
            handleTabItems(currentPage)
            handleIndicator(currentPage)
        }
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        Bundle.main.loadNibNamed(self.className, owner: self, options: nil)
        self.addSubview(view)
        view.autoPinEdgesToSuperviewEdges()
    }
    
    func layoutIndicator() {
        handleIndicator(currentPage, animated: true)
    }
    
    func setup(_ titleArray : [String], distrubition: TabDistrubition, startPage : Int = 0, headers : Bool = true) {
        if (headers) {
            setupButtons(titleArray, startPage: startPage, distrubition: distrubition)
        }
        self.headers = headers
    }
    
    fileprivate func setupButtons(_ titleArray: [String], startPage : Int, distrubition : TabDistrubition) {
        stackView.clearSubviews()
        arrTabButtons = []
        self.distrubition = distrubition
        guard titleArray.count > startPage else { return }
        switch distrubition {
        case .proportionally:
            self.constContentWidth.priority = .defaultLow
            self.stackView.distribution = .fillProportionally
            self.stackView.spacing = 36
            break
        case .equal(let spacing):
            self.constContentWidth.priority = .defaultHigh
            self.stackView.distribution = .fillEqually
            self.stackView.spacing = spacing
            break
        }
        titleArray.forEachEnumerated { (index, title) in
            let button = self.buildTabItem(title, index: index, startIndex: startPage)
            self.arrTabButtons.append(button)
            self.stackView.addArrangedSubview(button)
        }
        currentPage = startPage
        handleIndicator(currentPage, animated: false)
        layoutIfNeeded()
    }
    
    fileprivate func handleIndicator(_ index: Int, animated:Bool = true) {
        guard arrTabButtons.count > index else { return }
        let button = arrTabButtons[index]
        self.constIndicatorWidth.constant = button.w
        self.constIndicatorLeading.constant = button.x + stackView.x
        
        if animated {
            UIView.animate(withDuration: 0.3, delay: 0, usingSpringWithDamping: 0.8, initialSpringVelocity: 0, options: .curveEaseInOut, animations: {
                self.scrollContentView.layoutIfNeeded()
            }, completion: nil)
        } else {
            self.scrollContentView.layoutIfNeeded()
        }
    }
    
    fileprivate func handleTabItems(_ index: Int) {
        guard arrTabButtons.count > index else { return }
        arrTabButtons.forEach { (button) in
            button.isSelected = false
        }
        arrTabButtons[index].isSelected = true
    }

}

fileprivate extension ScrollableTabView {
    
    func buildTabItem(_ title:String, index:Int, startIndex:Int) -> UIButton {
        let tabButton = BaseUIButtonRegular()
        tabButton.titleLabel?.font = UIFont.mbtRegular(18)
        if isProportionally {
            tabButton.contentEdgeInsets = UIEdgeInsets.init(top: 0, left: 12, bottom: 0, right: 12)
        }
        tabButton.setTitleColor(UIColor.white.withAlphaComponent(0.5), for: .normal)
        tabButton.setTitleColor(UIColor.white, for: .selected)
        tabButton.setTitle(title, for: .normal)
        tabButton.setTitle(title, for: .selected)
        tabButton.addTarget(self, action: #selector(tabItemSelected(_:)), for: .touchUpInside)
        tabButton.tag = index+1
        tabButton.isSelected = index == startIndex
        return tabButton
    }
    
    @objc func tabItemSelected(_ sender : UIButton) {
        let normalizedIndex = sender.tag - 1
        if normalizedIndex != currentPage {
            let isForward = normalizedIndex > currentPage
            delegate?.scrollableTabView(self, didSelectItemAt: normalizedIndex, isForward: isForward)
            handleTabItems(normalizedIndex)
            handleIndicator(normalizedIndex)
            currentPage = normalizedIndex
        }
    }
    
    
    func scrollContentIfNeccessary(_ previousIndex:Int, nextIndex:Int) {
        guard isProportionally, arrTabButtons.count > previousIndex, arrTabButtons.count > nextIndex, previousIndex != nextIndex else { return }
        if nextIndex == 0 { scrollView.setContentOffset(.zero, animated: true); return }
        if nextIndex == arrTabButtons.count - 1 { scrollView.setContentOffset(CGPoint(x: stackView.right - bounds.w + stackView.x, y: 0), animated: true); return }
        if previousIndex > nextIndex {
            let itemIndexToMove = nextIndex - 1
            let itemToMove = arrTabButtons[itemIndexToMove]
            let leftPoint = itemToMove.x
            let targetOffset = leftPoint
            if scrollView.contentOffset.x > targetOffset { scrollView.setContentOffset(CGPoint(x: targetOffset, y: 0), animated: true) }
        } else {
            let itemIndexToMove = nextIndex + 1
            let itemToMove = arrTabButtons[itemIndexToMove]
            let rightPoint = itemToMove.right
            let targetOffset = rightPoint - bounds.w
            if targetOffset > 0 && scrollView.contentOffset.x < targetOffset { scrollView.setContentOffset(CGPoint(x: targetOffset + stackView.x, y: 0), animated: true) }
        }
    }
    
    fileprivate var isProportionally : Bool {
        switch distrubition {
        case .proportionally: return true
        default: return false
        }
    }
}
