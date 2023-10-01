//
//  UIViewController+Extensions.swift
//
//  Created by Serkut Yegin.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

enum Storyboard : String {
    
    //These are the Storyboard names
    case main = "Main"
    case login = "Login"
    case portalNews = "Portal_News"
    case portalLocation = "Portal_Location"
    case portalTransportation = "Portal_Transportation"
    case portalMenu = "Portal_Menu"
    case notifications = "Notifications"
    case profile = "Profile"
    case popups = "Popups"
    
    enum Identifier : String {
        
        //These are the ViewController Storyboard Identifiers
        case tabbar = "tabbarVC"
        case signInNav = "signInNav"
        case termsOfUseNav = "termsOfUseNav"
        case webviewNav = "webviewNav"
        case welcomeNav = "welcomeNav"
        case termsOfUse = "termsOfUseVC"
        case webview = "webviewVC"
        case about = "aboutVC"
        case settings = "settingsVC"
        case activationCode = "activationCodeVC"
        case home = "homeVC"
        case profile = "profileVC"
        case notif = "notifVC"
        case pageContainer = "pageContainerVC"
        case news = "newsVC"
        case popupContainer = "popupContainerVC"
        case technicalSupport = "technicalSupportVC"
        case driverInfo = "driverInfoVC"
        case newsDetail = "newsDetailVC"
        case birthdayList = "birthdayListVC"
        case mealMenu = "mealMenuVC"
        case transportationOptions = "transportationOptionsVC"
        case transportation = "transportationVC"
        case transportationContent = "transportationContentVC"
        case transportationSearch = "transportationSearchVC"
        case flexibleWork = "flexibleWorkVC"
        case flexibleWorkDetail = "flexibleWorkDetailVC"
        case workingCalendar = "workingCalendarVC"
        case location = "locationVC"
        case locationSearch = "locationSearchVC"
        case locationResult = "locationResultVC"
        
        //Storyboards owning viewcontrollers
        var storyboardName : Storyboard {
            switch self {
            case .tabbar:
                return .main
            case .welcomeNav, .activationCode:
                return .login
            case .notif:
                return notifications
            case .home, .news, .newsDetail, .birthdayList:
                return .portalNews
            case .popupContainer, .technicalSupport, .driverInfo:
                return .popups
            case .mealMenu:
                return .portalMenu
            case .transportation, .transportationSearch, .transportationOptions:
                return .portalTransportation
            case .profile, .workingCalendar, .flexibleWork, .flexibleWorkDetail, .settings:
                return .profile
            case .location, .locationSearch, .locationResult:
                return .portalLocation
            default: return .main
            }
        }
    }
}

protocol ModelType { }
extension UIViewController : ModelType { }
extension ModelType where Self : UIViewController {
    static func fromNib(_ nibName : String = String(describing: Self.self)) -> Self {
        return Self(nibName: nibName, bundle: nil)
    }
    
    static func fromStoryboard(_ type : Storyboard.Identifier) -> Self {
        let storyboard : UIStoryboard = UIStoryboard(name: type.storyboardName.rawValue, bundle: nil)
        return storyboard.instantiateViewController(withIdentifier: type.rawValue) as! Self
    }
}

extension UIViewController {
    
    public func addController(toParent controller:UIViewController,toView:UIView) {
        controller.addChildViewController(self)
        toView.addSubview(self.view)
        self.view.autoPinEdgesToSuperviewEdges()
        self.didMove(toParentViewController: controller)
    }
    
    public func removeControllerFromParent() {
        self.willMove(toParentViewController: nil)
        self.view.removeFromSuperview()
        self.removeFromParentViewController()
    }
    
    public var presented : UIViewController {
        guard let presentedVC = presentedViewController else { return self }
        return presentedVC
    }
    
    public func close() {
        if presentingViewController != nil {
            dismiss(animated: true, completion: nil)
        } else {
            navigationController?.popViewController(animated: true)
        }
    }
    
    public func pushControllerExceptMe(_ controller : UIViewController) {
        guard let viewControllers = navigationController?.viewControllers, viewControllers.count > 0 else { return }
        var mutable = viewControllers
        mutable.removeLast()
        mutable.append(controller)
        self.navigationController?.setViewControllers(mutable, animated: true)
    }
}

extension UIViewController : UIPopoverPresentationControllerDelegate {
    
    public func presentPopover(popover:UIViewController, from view:UIView, preferredContentSize:CGSize? = nil, direction:UIPopoverArrowDirection = .any) {
        popover.modalPresentationStyle = .popover
        if preferredContentSize != nil {
            popover.preferredContentSize = preferredContentSize!
        }
        let controller = popover.popoverPresentationController
        controller?.backgroundColor = UIColor.white
        controller?.delegate = self
        controller?.sourceView = view
        controller?.sourceRect = CGRect(x: view.w/2, y: 1, w: 1, h: view.h)
        controller?.permittedArrowDirections = direction
        self.present(popover, animated: true, completion: nil)
    }
    
    public func adaptivePresentationStyle(for controller: UIPresentationController) -> UIModalPresentationStyle {
        return .none
    }
    
}
