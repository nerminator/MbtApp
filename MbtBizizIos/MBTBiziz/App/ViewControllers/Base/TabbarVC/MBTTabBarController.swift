//
//  MBTTabBarController.swift
//  MBT
//
//  Created by Serkut Yegin on 2.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class MBTTabBarController: UITabBarController {
    
    internal final let tabbarDelegate = MBTTabBarControllerDelegate()
    
    var homeVC : HomeViewController {
        return (self.viewControllers![0] as! BaseNavCon).viewControllers[0] as! HomeViewController
    }
    
    var notificationsVC : NotificationsViewController {
        return (self.viewControllers![1] as! BaseNavCon).viewControllers[0] as! NotificationsViewController
    }
    
    var profileVC : ProfileViewController {
        return (self.viewControllers![2] as! BaseNavCon).viewControllers[0] as! ProfileViewController
    }
    
    var unreadedNotificationCount : Int = 0 {
        didSet {
            if let item = tabBar.items?[1] {
                item.badgeColor = UIColor.mbtBlue
                item.badgeValue = unreadedNotificationCount == 0 ? nil : "\(unreadedNotificationCount)"
                repositionBadge()
            }
            if selectedIndex == 1 && unreadedNotificationCount > 0 {
                notificationsVC.interactor?.updatePage(request: Notifications.Update.Request())
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        for item in tabBar.items! {
            item.image = item.image?.withRenderingMode(.alwaysOriginal)
        }
        self.delegate = tabbarDelegate
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        MBTNotificationManager.shared.activateCachedNotificationIfNeccesary()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func repositionBadge(){
        for tabBarItem in self.tabBar.subviews {
            for badgeView in tabBarItem.subviews where NSStringFromClass(badgeView.classForCoder) == "_UIBadgeView" {
                badgeView.layer.transform = CATransform3DIdentity
                badgeView.layer.transform = CATransform3DMakeTranslation(-5.0, 1.0, 1.0)
            }
        }
    }
}
