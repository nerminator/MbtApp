//
//  NetworkActivityManager.swift
//
//  Created by Serkut Yegin.
//

import UIKit

class NetworkActivityManager {

    static let sharedManager = NetworkActivityManager()
    //fileprivate var alertNotReachable : TSAlertView?
    fileprivate var isNotReachablePopupOnScreen = false
    fileprivate let loadingView = LoadingView.fromNib()
    
    
    private var activityCount = 0 {
        didSet {
            DispatchQueue.main.async { [unowned self] in
                UIApplication.shared.isNetworkActivityIndicatorVisible = self.activityCount > 0
            }
        }
    }
    
    private var loadingCount = 0 {
        didSet {
            DispatchQueue.main.async { [unowned self] in
                self.loadingView.isOnFire = self.loadingCount > 0
            }
        }
    }
    
    func showNetworkActivity() {
        activityCount += 1
    }
    
    func hideNetworkActivity() {
        activityCount = 0
    }
    
    func pushNetworkActivity(_ target : Loading) {
        activityCount += 1
        if target.needToShowLoading { loadingCount += 1 }
    }
    
    func popNetworkActivity(_ target : Loading) {
        if activityCount > 0 { activityCount -= 1 }
        if target.needToShowLoading && loadingCount > 0 { loadingCount -= 1 }
    }
    
    func showNotReachablePopup() {
        /*
        if isNotReachablePopupOnScreen { return }
        
        if alertNotReachable == nil {
            alertNotReachable = TSAlertView.build("Hata", message: "İnternet erişimi yok.", cancelAction: TSAlertAction(style: .done, action: { [weak self] in
                self?.isNotReachablePopupOnScreen = false
            })).show()
        }
        
        isNotReachablePopupOnScreen = true
        
        DispatchQueue.main.async { [weak self] in
            guard let alertToShow = self?.alertNotReachable else { self?.isNotReachablePopupOnScreen = false; return }
            alertToShow.show()
        }*/
    }
}
