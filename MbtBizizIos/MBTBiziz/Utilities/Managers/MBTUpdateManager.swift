//
//  MBTUpdateManager.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 5.01.2019.
//  Copyright © 2019 Serkut Yegin. All rights reserved.
//

import UIKit

class MBTUpdateManager {
    
    static let shared = MBTUpdateManager()
    
    func checkForUpdate(_ completion: @escaping VoidClosure) {
        
        fetchVersionInfo(completion)
    }
    
    private func fetchVersionInfo(_ completion: @escaping VoidClosure) {
        
        WSProvider.shared.wsRequest(
            .initCall(versionNumber: MBTConstants.Device.releaseVersionNumber!),
            success: { [unowned self] (response: WSResponse<InitResponse>) in
                
                if let versionData = response.data, versionData.buttonList != nil, !versionData.buttonList!.isEmpty {
                    self.presentVersionAlert(with: versionData, completion: completion)
                } else {
                    completion()
                }
        }) { (error) in
            completion()
        }
    }
    
    private func presentVersionAlert(
        with versionData: InitResponse,
        completion: @escaping VoidClosure
        ) {
        
        let controller = UIAlertController(
            title: versionData.title,
            message: versionData.message,
            preferredStyle: .alert
        )
        
        versionData.buttonList?.forEach({ (item) in
            
            let action = UIAlertAction(
                title: item.text,
                style: .default,
                handler: { [unowned self] (action) in
                    if self.goToLinkIfNeccesary(item) {
                        self.presentVersionAlert(with: versionData, completion: completion)
                    } else {
                        completion()
                    }
            })
            controller.addAction(action)
        })
        controller.show()
    }
    
    private func goToLinkIfNeccesary(_ item: ButtonItem) -> Bool {
        
        if let itemType = item.type, itemType == .download, let urlStr = item.url, let url = URL(string: urlStr) {
            if UIApplication.shared.canOpenURL(url) {
                UIApplication.shared.open(url)
                return true
            }
        }
        return false
    }
}
