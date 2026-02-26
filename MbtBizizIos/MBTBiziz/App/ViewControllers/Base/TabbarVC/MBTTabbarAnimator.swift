//
//  MBTTabbarAnimator.swift
//  MBT
//
//  Created by Serkut Yegin on 29.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class MBTTabBarControllerDelegate: NSObject, UITabBarControllerDelegate {
    
    func tabBarController(_ tabBarController: UITabBarController, animationControllerForTransitionFrom fromVC: UIViewController, to toVC: UIViewController) -> UIViewControllerAnimatedTransitioning? {
        return MBTTabbarAnimator(tabBarController: tabBarController, lastIndex: tabBarController.selectedIndex)
    }
    
    func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
        guard let selectedViewController = tabBarController.selectedViewController else { return true }
        if selectedViewController == viewController {
            if let navCon = selectedViewController as? UINavigationController, let baseHome = navCon.viewControllers[0] as? MBTBaseHomeViewController {
                baseHome.scrollToTop()
            }
        }
        return true
    }
}

class MBTTabbarAnimator: NSObject, UIViewControllerAnimatedTransitioning {
    weak var transitionContext: UIViewControllerContextTransitioning?
    var tabBarController: UITabBarController!
    var lastIndex = 0
    
    func transitionDuration(using transitionContext: UIViewControllerContextTransitioning?) -> TimeInterval {
        return 0.4
    }
    
    init(tabBarController: UITabBarController, lastIndex: Int) {
        self.tabBarController = tabBarController
        self.lastIndex = lastIndex
    }
    
    func animateTransition(using transitionContext: UIViewControllerContextTransitioning) {
        self.transitionContext = transitionContext
        
        let containerView = transitionContext.containerView
        let fromViewController = transitionContext.viewController(forKey: UITransitionContextViewControllerKey.from)
        let toViewController = transitionContext.viewController(forKey: UITransitionContextViewControllerKey.to)
        
        containerView.addSubview(toViewController!.view)
        
        var viewWidth = toViewController!.view.bounds.width
        
        if tabBarController.selectedIndex > lastIndex {
            viewWidth = -viewWidth
        }
        
        var transform = CATransform3DIdentity
        transform.m34 = 1.0 / 200
        transform = CATransform3DScale(transform, 2.0, 1.0, 1.0)
        transform = CATransform3DTranslate(transform, viewWidth, 0, 0)
        toViewController!.view.layer.transform = transform
        
        UIView.animate(withDuration: self.transitionDuration(using: (self.transitionContext)), delay: 0.0, usingSpringWithDamping: 0.8, initialSpringVelocity: 0, options: .overrideInheritedOptions, animations: {
            toViewController!.view.transform = CGAffineTransform.identity
            fromViewController!.view.transform = CGAffineTransform(translationX: -viewWidth, y: 0)
            fromViewController!.view.transform = CGAffineTransform(scaleX:0.8, y:0.8)
            fromViewController!.view.alpha = 0.8
        }, completion: { _ in
            self.transitionContext?.completeTransition(!self.transitionContext!.transitionWasCancelled)
            fromViewController!.view.transform = CGAffineTransform.identity
            fromViewController!.view.alpha = 1
        })
    }
}
