//
//  NotificationsWorker.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class NotificationsWorker
{
    func doSetup(_ viewController : NotificationsViewController) {
        let interactor = NotificationsInteractor()
        let presenter = NotificationsPresenter()
        let router = NotificationsRouter()
        viewController.interactor = interactor
        viewController.router = router
        interactor.presenter = presenter
        presenter.viewController = viewController
        router.viewController = viewController
        router.dataStore = interactor
    }
    
    fileprivate var currentLastId : String?
    fileprivate var currentFirstId : String?
    fileprivate var initialNotificationsRunning = false
    
    // MARK: - Business Logic
    
    func getInitialNotifications(completion:@escaping (_ notifications:[MBTNotificationListItem])->()) {
        initialNotificationsRunning = true
        WSProvider.shared.wsRequest(.getNotificationList(firstDate: nil, lastDate: nil), success: { [weak self] (response : WSResponse<[MBTNotificationListItem]>) in
            completion(response.data ?? [])
            self?.initialNotificationsRunning = false
        }) { [weak self] (error) in
            self?.initialNotificationsRunning = false
        }
    }
    
    func getNewerNotifications(notificationId:String?,completion:@escaping (_ notifications:[MBTNotificationListItem])->()) {
        if initialNotificationsRunning { return }
        if currentLastId == nil {
            currentLastId = notificationId
            WSProvider.shared.wsRequest(.getNotificationList(firstDate: nil, lastDate: notificationId), success: { [weak self] (response : WSResponse<[MBTNotificationListItem]>) in
                completion(response.data ?? [])
                self?.currentLastId = nil
            }) { [weak self] (error) in
                self?.currentLastId = nil
            }
        }
    }
    
    func getPreviousNotifications(notificationId:String?, completion:@escaping (_ notifications:[MBTNotificationListItem])->()) {
        if initialNotificationsRunning { return }
        if currentFirstId == nil || currentFirstId != notificationId {
            currentFirstId = notificationId
            WSProvider.shared.wsRequest(.getNotificationList(firstDate: notificationId, lastDate: nil), success: { (response : WSResponse<[MBTNotificationListItem]>) in
                completion(response.data ?? [])
            }) { (error) in }
        }
    }
    
    func deleteNotification(notificationId:Int, onSuccess:@escaping ()->()) {
        WSProvider.shared.wsRequest(.deleteNotification(notificationId: notificationId), success: { (response : WSResponse<MarshalResponse>) in
            if response.isSuccess {
                onSuccess()
            }
        }) { (error) in }
    }
    
    /*func resetNotifications() {
        MBTProvider.shared.mbtRequest(.resetHasNotification) { (response) in }
    }*/
}
