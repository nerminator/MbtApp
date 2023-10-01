//
//  TokenManager.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class TokenManager {

    static let sharedManager = TokenManager()
    
    var token : String {
        return _token ?? ""
    }
    fileprivate var _token : String? = UserDefaults.standard.object(forKey: MBTConstants.UserPreference.Token) as? String {
        didSet {
            UserDefaults.standard.set(_token, forKey: MBTConstants.UserPreference.Token)
        }
    }
    
    func clearToken() {
        _token = nil
    }
    
    func handleLoginResponse(_ loginResponse : MBTLoginResponse) {
        guard let token = loginResponse.token else { return }
        _token = token
    }
}
