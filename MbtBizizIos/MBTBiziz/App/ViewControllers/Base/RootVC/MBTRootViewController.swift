//
//  MBTRootViewController.swift
//  MBT
//
//  Created by Serkut Yegin on 2.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class MBTRootViewController: MBTBaseViewController {

    @IBOutlet weak var backgroundView: UIView!

    private var isLegitimateToContinue: Bool = true

    private var _controller : UIViewController?
    var controller : UIViewController? {
        set {
            if let newValue = newValue {
                _controller?.removeControllerFromParent()
                newValue.addController(toParent: self, toView: self.view)
                _controller = newValue
            } else {
                _controller?.removeControllerFromParent()
                _controller = nil
            }
        }
        get {
            return _controller
        }
    }
    
    override func viewDidLoad() {

        super.viewDidLoad()
        WSProvider.shared.wsRequest(.appStartup, success: { (response: MBTAppStartupResponse?) in
            MBTConstants.aboutText = response?.aboutText ??  ""
            MBTConstants.appDescriptionText = response?.appDescriptionText ??  ""
        })
        
        if JailbreakHelper.isJailbroken() {
            isLegitimateToContinue = false
        } else {
            MBTUpdateManager.shared.checkForUpdate { [weak self] in
                self?.continueToApplication()
            }
        }
    }
    
    private func continueToApplication() {
        
        backgroundView.isHidden = true
        
        TokenManager.sharedManager.signinSilently({ success in
            if success  {
                NavigationHelper().showHomeScreen()
            } else {
                self.showOidcScreen()
            }
        })
        
    }
    
    override func viewDidAppear(_ animated: Bool) {

        super.viewDidAppear(animated)

        if isLegitimateToContinue {
            if MBTSingleton.shared.isFirstOpen {
                presentTermsOfUse()
            }
        } else {
            showBasicAlert(message: "TXT_COMMON_JAILBREAK".localized()) {
                exit(0)
            }
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func presentTermsOfUse() {
        controller?.present(SignInNavCon.fromStoryboard(.termsOfUseNav), animated: true, completion: nil)
    }
    
    func showWelcomeScreen() {
        controller = SignInNavCon.fromStoryboard(.welcomeNav)
    }
    
    func showOidcScreen() {
        controller = SignInNavCon.fromStoryboard(.welcomeNav)
    }
    
    func showHomeScreen(/*baseResponse : MBTBaseResponse? = nil*/) {
        let tabbar = MBTTabBarController.fromStoryboard(.tabbar)
        //tabbar.baseResponse = baseResponse
        controller = tabbar
    }
}
