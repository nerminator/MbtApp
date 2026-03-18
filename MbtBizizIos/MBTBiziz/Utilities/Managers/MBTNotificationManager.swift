//
//  MBTNotificationManager.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 14.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

struct MBTNotificationPayload {
    let body : String
    let title : String
    let extraPayload : [String:Any]?
    
    init(userInfo : [AnyHashable : Any]) {
        
        if let aps = userInfo[AnyHashable("aps")] as? NSDictionary,
            let alert = aps["alert"] as? NSDictionary,
            let body = alert["body"] as? String,
            let title = alert["title"] as? String {
            self.body = body
            self.title = title
        } else {
            self.body = ""
            self.title = ""
        }
        
        if let payload = userInfo[AnyHashable("extraPayLoad")] as? [String : Any] {
            self.extraPayload = payload
        } else {
            self.extraPayload = nil
        }
    }
}

class MBTNotificationManager {

    static let shared = MBTNotificationManager()
    fileprivate var cachedNotification : MBTNotificationListItem?
    
    var unreadedNotificationCount : Int = 0 {
        didSet {
            NavigationHelper().tabbarVC?.unreadedNotificationCount = unreadedNotificationCount
        }
    }
    
    func handleNotificationReceivedPayload(_ payload : MBTNotificationPayload) {
        guard let parsed = parseNotification(payload) else { return }
        if let _ = NavigationHelper().rootVC.controller as? MBTTabBarController {
            self.unreadedNotificationCount += 1
        } else {
            self.cachedNotification = parsed
        }
    }
    
    func handleNotificationOpenedPayload(_ payload : MBTNotificationPayload) {
        guard let parsed = parseNotification(payload) else { return }
        
        if let _ = NavigationHelper().rootVC.controller as? MBTTabBarController {
            showNotificationDetail(parsed)
        } else {
            self.cachedNotification = parsed
        }
    }
    
    fileprivate func showNotificationDetail(_ notification : MBTNotificationListItem) {
        if let presented = NavigationHelper().rootVC.presentedViewController {
            presented.dismiss(animated: false, completion: { [unowned self] in
                self.redirectNotification(notification)
            })
        } else {
            redirectNotification(notification)
        }
    }
    
    func activateCachedNotificationIfNeccesary() {
        if let cached = cachedNotification {
            showNotificationDetail(cached)
            self.cachedNotification = nil
        }
    }
    
    func showPushNotificationInfoAlert(_ completion: @escaping ()->()) {
        let controller = UIAlertController(title: "TXT_COMMON_INFO".localized(), message: "TXT_INFO_NOTIFICATION".localized(), preferredStyle: .alert)
        controller.addAction(UIAlertAction.init(title: "TXT_COMMON_YES".localized(), style: .default, handler: { (action) in
            completion()
        }))
        controller.addAction(UIAlertAction.init(title: "TXT_COMMON_NO".localized(), style: .default, handler: { (action) in
            completion()
        }))
        DispatchQueue.main.async {
            UIApplication.shared.keyWindow?.rootViewController?.present(controller, animated: true, completion: nil)
        }
    }
}

fileprivate extension MBTNotificationManager {
    
    //MARK: Received Notification Handlers
    
    func parseNotification(_ payload : MBTNotificationPayload) -> MBTNotificationListItem? {
        guard let data = payload.extraPayload else { return nil }
        #if DEBUG
        debugPrint(data)
        #endif
        return MBTNotificationListItem.init(object: data)
    }
    
    func redirectNotification(_ notification : MBTNotificationListItem) {
        let detail = NewsDetailViewController.fromStoryboard(.newsDetail)
        var destination = detail.router?.dataStore
        destination?.listItem = notification
        detail.fromPushNotification = true
        let navCon = SignInNavCon.init(rootViewController: detail)
        NavigationHelper().rootVC.present(navCon, animated: true, completion: nil)
    }
}
