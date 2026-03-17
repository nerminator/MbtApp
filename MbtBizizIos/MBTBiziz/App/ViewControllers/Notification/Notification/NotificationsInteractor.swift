//
//  NotificationsInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol NotificationsBusinessLogic
{
    func initializeView(request : Notifications.Initialize.Request)
    func validateInputs(request : Notifications.Validate.Request)
    func updatePage(request : Notifications.Update.Request)
    func reloadPage(request : Notifications.Reload.Request)
    func sendData(request : Notifications.SendData.Request)
    func resetNotifications()
    
    var arrNotifications : [MBTNotificationListItem] { get set }
}

protocol NotificationsDataStore
{

}

class NotificationsInteractor: NotificationsBusinessLogic, NotificationsDataStore
{
    var presenter: NotificationsPresentationLogic?
    var worker = NotificationsWorker()
    var arrNotifications: [MBTNotificationListItem] = []
    
    func initializeView(request : Notifications.Initialize.Request) {
        worker.getInitialNotifications { [weak self] (notifications) in
            self?.arrNotifications.append(contentsOf: notifications)
            self?.presenter?.presentInitializeViewResult(response: Notifications.Initialize.Response())
        }
    }
    
    func validateInputs(request : Notifications.Validate.Request) {
        presenter?.presentValidateInputs(response: Notifications.Validate.Response())
    }
    
    func updatePage(request: Notifications.Update.Request) {
        worker.getNewerNotifications(notificationId: arrNotifications.first?.notificationTime) { [weak self] (notifications) in
            self?.arrNotifications.insert(contentsOf: notifications, at: 0)
            self?.presenter?.presentUpdatePageResult(response: Notifications.Update.Response(numberOfNewNotifications: notifications.count))
        }
    }
    
    func reloadPage(request : Notifications.Reload.Request) {
        worker.getPreviousNotifications(notificationId: arrNotifications.last?.notificationTime) { [weak self] (notifications) in
            self?.arrNotifications.append(contentsOf: notifications)
            self?.presenter?.presentReloadPageResult(response : Notifications.Reload.Response())
        }
    }
    
    func sendData(request : Notifications.SendData.Request) {
        guard let notificationId = arrNotifications[request.indexPath.row].id else { return }
        worker.deleteNotification(notificationId: notificationId) { [weak self] in
            if let index = self?.findNotificationIndex(notificationId: notificationId) {
                self?.arrNotifications.remove(at: index)
                self?.presenter?.presentSendDataResult(response : Notifications.SendData.Response(index: index))
            }
        }
    }
    
    func resetNotifications() {
        for notification in arrNotifications {
            if notification.isSeen == false {
                notification.seen = true
            }
        }
    }
  
}

extension NotificationsInteractor {
    
    fileprivate func findNotificationIndex(notificationId:Int) -> Int? {
        for index in 0..<arrNotifications.count {
            if let id = arrNotifications[index].id, id == notificationId {
                return index
            }
        }
        return nil
    }
}
