//
//  OidcViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 14.08.2024.
//  Copyright © 2024 Onion TEcj. All rights reserved.
//

import Foundation

class OidcViewController: MBTBaseViewController, URLSessionDelegate
{
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }

    @IBAction func signInClicked(_ sender: Any) {
        TokenManager.sharedManager.acquireTokenInteractively(self, { (success, errorText) in
            if (success) {
                NavigationHelper().showHomeScreen()
            } else {
                self.showBasicAlert( message: errorText)
            }
                
        })
    }
    
}
