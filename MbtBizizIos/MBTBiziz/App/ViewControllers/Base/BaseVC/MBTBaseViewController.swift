//
//  MBTBaseViewController.swift
//  MBT
//
//  Created by Serkut Yegin on 2.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class MBTBaseViewController: UIViewController {

    var additionalData : [String:Any] = [:]
    internal var firstWillAppear = true
    internal var firstDidAppear = true
    internal var isAppeared = false
    internal var didAppeared = false
    
    fileprivate var navBarSeperator : UIView?
    internal var showNavbarSeperator = false {
        willSet {
            if newValue != showNavbarSeperator {
                if newValue {
                    navBarSeperator?.showAnimated()
                } else {
                    navBarSeperator?.hideAnimated()
                }
            }
        }
    }
    
    internal lazy var infoBarButton : UIBarButtonItem = {
        let barButton =  UIBarButtonItem.init(image: #imageLiteral(resourceName: "btnAbout"), style: .plain, target: self, action: #selector(barButtonItemInfoTapped(_:)))
        //ibarButton.imageInsets =  UIEdgeInsetsMake(10,10,10,10)
  
        return barButton
    }()
    
    internal var refreshControl : UIRefreshControl?
    
    fileprivate var backgroundGradient : CAGradientLayer?
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        set {}
        get {
            return .lightContent
        }
    }
    
    deinit {
        #if DEBUG
        debugPrint("\(NSStringFromClass(self.classForCoder)) deinitialized")
        #endif
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if self.shouldAddNavBarSeperator() {
            addNavBarSeperator()
        }
        
        if let tableView = shouldAddRefreshControl() {
            refreshControl = UIRefreshControl()
            refreshControl?.attributedTitle = NSAttributedString.init(string: "TXT_COMMON_UPDATING".localized(), attributes: [.font : UIFont.mbtDemi(12)!,.foregroundColor : UIColor.white])
            refreshControl?.tintColor = UIColor.white
            refreshControl?.addTarget(self, action: #selector(refreshTableViewTrigerred(_:)), for: .valueChanged)
            tableView.refreshControl = refreshControl
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        if self.shouldListenKeyboardNotification() {
            NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShown(_:)), name: .UIKeyboardWillShow, object: nil)
            NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide(_:)), name: .UIKeyboardWillHide, object: nil)
        }
        self.firstWillAppear = false
        self.isAppeared = true
        if shouldAddGradientLayer() {
            setGradientBackground()
        }
        if shouldAddInfoBarButton() {
            if self.navigationItem.rightBarButtonItem == nil  {
                self.navigationItem.rightBarButtonItem = infoBarButton
            } else if !( self.navigationItem.rightBarButtonItems?.contains( infoBarButton ))! {
                self.navigationItem.rightBarButtonItems?.append( infoBarButton )
            }
        }
        
        if let tableView = shouldAddRefreshControl(), let refreshControl = refreshControl, refreshControl.isRefreshing {
            let offset = tableView.contentOffset
            self.refreshControl?.endRefreshing()
            self.refreshControl?.beginRefreshing()
            tableView.contentOffset = offset
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        self.firstDidAppear = false
        self.didAppeared = true
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        if self.shouldListenKeyboardNotification() {
            NotificationCenter.default.removeObserver(self, name: .UIKeyboardWillShow, object: nil)
            NotificationCenter.default.removeObserver(self, name: .UIKeyboardWillHide, object: nil)
        }
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        self.isAppeared = false
        self.didAppeared = false
    }
    
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
        backgroundGradient?.frame = self.view.bounds
    }
    
    //MARK: Keyboard Notifations
    
    @objc private func keyboardWillShown(_ notification:Notification) {
        self.handleKeyboardEvent(notification: notification, willShow: true)
    }
    
    @objc private func keyboardWillHide(_ notification:Notification) {
        self.handleKeyboardEvent(notification: notification, willShow: false)
    }
    
    private func handleKeyboardEvent(notification:Notification, willShow:Bool) {
        
        if let userInfo = notification.userInfo {
            let endFrame = (userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue
            let duration:TimeInterval = (userInfo[UIKeyboardAnimationDurationUserInfoKey] as? NSNumber)?.doubleValue ?? 0
            let animationCurveRawNSN = userInfo[UIKeyboardAnimationCurveUserInfoKey] as? NSNumber
            let animationCurveRaw = animationCurveRawNSN?.uintValue ?? UIViewAnimationOptions.curveEaseInOut.rawValue
            let animationCurve:UIViewKeyframeAnimationOptions = UIViewKeyframeAnimationOptions(rawValue: animationCurveRaw)
            self.keyboardNotification(willShow: willShow, endFrame: endFrame!, duration: duration, animationCurve: animationCurve)
        }
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        coordinator.animate(alongsideTransition: { [weak self] (context) in
            self?.backgroundGradient?.frame = CGRect(x: 0, y: 0, w: size.width, h: size.height)
        }, completion: nil)
        
    }
}

extension MBTBaseViewController {
    
    @objc func shouldListenKeyboardNotification() -> Bool {
        return false
    }
    
    @objc func shouldAddGradientLayer() -> Bool {
        return true
    }
    
    @objc func shouldAddNavBarSeperator() -> Bool {
        return false
    }
    
    @objc func shouldAddInfoBarButton() -> Bool {
        return true
    }
    
    @objc func shouldAddRefreshControl() -> UITableView? {
        return nil
    }
    
    @objc func refreshTableViewTrigerred(_ sender:AnyObject) {
        
    }
    
    func endRefreshing() {
        self.refreshControl?.endRefreshing()
    }
    
    func addCloseBarButtonItem() {
        let barButton = UIBarButtonItem(title: "TXT_COMMON_CLOSE".localized(), style: .plain, target: self, action: #selector(barButtonItemCloseTapped(_:)))
        barButton.tintColor = UIColor.white
        navigationItem.leftBarButtonItem = barButton
    }
    
    @objc func barButtonItemCloseTapped(_ sender : UIBarButtonItem) {
        dismiss(animated: true, completion: nil)
    }
    
    @objc func barButtonItemInfoTapped(_ sender :  UIBarButtonItem) {
        self.navigationController?.pushViewController(AboutViewController.fromStoryboard(.about), animated: true)
    }
    
    fileprivate func keyboardNotification(willShow:Bool,endFrame:CGRect,duration:TimeInterval,animationCurve:UIViewKeyframeAnimationOptions) {
        if didAppeared {
            UIView.animateKeyframes(withDuration: duration, delay: 0, options: animationCurve, animations: {
                self.keyboardAnimation(willShow: willShow, endFrame: endFrame)
                self.view.layoutIfNeeded()
            }) { (finished) in }
        } else {
            self.keyboardAnimation(willShow: willShow, endFrame: endFrame)
        }
    }
    
    @objc func keyboardAnimation(willShow:Bool, endFrame:CGRect) {
        
    }
    
    fileprivate func addNavBarSeperator() {
        
        let seperator = UIView()
        view.addSubview(seperator)
        seperator.isHidden = true
        view.bringSubview(toFront: seperator)
        seperator.backgroundColor = UIColor.white.withAlphaComponent(0.2)
        seperator.autoSetDimension(.height, toSize: 1)
        seperator.autoPinEdge(.leading, to: .leading, of: view)
        seperator.autoPinEdge(.trailing, to: .trailing, of: view)
        seperator.autoPin(toTopLayoutGuideOf: self, withInset: 0)
        navBarSeperator = seperator
    }
    
    fileprivate func setGradientBackground() {
        if backgroundGradient == nil {
            
            let colorDark = UIColor(hexString: "#000000")!.cgColor
            let color30Percent =  UIColor(hexString: "#2e2e2e")!.cgColor
            let height = self.view.bounds.height
            
            let b1 = NSNumber( value: Float(1000/height))
            let b2 = NSNumber( value: Float(500/height))
            var bottomDarkStartLocation : NSNumber = height > 1000 ? b1 : b2
            if (bottomDarkStartLocation.floatValue > 1){
                bottomDarkStartLocation = 1
            }
            
            let lightStartLocation = NSNumber(value: Float(bottomDarkStartLocation.floatValue * 0.3))
            
            backgroundGradient = CAGradientLayer()
            backgroundGradient!.colors = [ colorDark, color30Percent, colorDark ]
            backgroundGradient!.locations = [ 0.0, lightStartLocation, bottomDarkStartLocation]
            backgroundGradient!.frame = self.view.bounds
            self.view.layer.insertSublayer(backgroundGradient!, at: 0)
            

        }
    }
    
    internal func showBasicAlert(
        with title:String? = "TXT_COMMON_INFO".localized(),
        message:String?,
        actionTitles:[String] = [],
        cancelTitle:String = "TXT_COMMON_DONE".localized(),
        cancelHandler: (() -> Void)? = nil
    ) {
        guard let message = message, message.length > 0 else { return }
        let controller = UIAlertController(title: title, message: message, preferredStyle: .alert)
        for actionTitle in actionTitles {
            controller.addAction(UIAlertAction(title: actionTitle, style: .default, handler: nil))
        }
        controller.addAction(UIAlertAction(title: cancelTitle, style: .cancel, handler: { (_) in
            cancelHandler?()
        }))
        present(controller, animated: true, completion: nil)
    }
    
}
