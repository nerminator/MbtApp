//
//  PopupContainerViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol PopupProtocol {
    var popupContainer : PopupContainerViewController? {get set}
}

extension UIViewController {
    
    func presentAsBottomPopup(_ identifier : Storyboard.Identifier) {
        let container = PopupContainerViewController.fromStoryboard(.popupContainer)
        container.content = UIViewController.fromStoryboard(identifier) as? (UIViewController&PopupProtocol)
        container.modalPresentationStyle = .overCurrentContext
        present(container, animated: false, completion: nil)
    }
    
    func presentAsBottomPopup(to controller : (UIViewController&PopupProtocol)) {
        let container = PopupContainerViewController.fromStoryboard(.popupContainer)
        container.content = controller
        container.modalPresentationStyle = .overCurrentContext
        present(container, animated: false, completion: nil)
    }
}

class PopupContainerViewController: UIViewController {

    @IBOutlet weak var viewBackground: UIView!
    @IBOutlet weak var viewContainer: UIView!
    @IBOutlet weak var constContainerBottom: NSLayoutConstraint!
    @IBOutlet var tapGesture: UITapGestureRecognizer!
    fileprivate var content : (UIViewController & PopupProtocol)?
    
    @IBAction func tapGestureRecognized(_ sender: UITapGestureRecognizer) {
        sender.isEnabled = false
        dismiss()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        content?.popupContainer = self
        content?.addController(toParent: self, toView: viewContainer)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        constContainerBottom.constant = -16
        let animator = UIViewPropertyAnimator.init(duration: 0.5, dampingRatio: 0.8) {
            self.viewBackground.alpha = 1
            self.view.layoutIfNeeded()
        }
        animator.addCompletion { [weak self] (position) in
            if position == .end {
                self?.tapGesture.isEnabled = true
            }
        }
        animator.startAnimation()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func dismiss() {
        constContainerBottom.constant = -516
        let animator = UIViewPropertyAnimator.init(duration: 0.5, dampingRatio: 0.8) {
            self.viewBackground.alpha = 0
            self.view.layoutIfNeeded()
        }
        animator.addCompletion { [weak self] (position) in
            if position == .end {
                self?.dismiss(animated: false, completion: nil)
            }
        }
        animator.startAnimation()
    }

}
