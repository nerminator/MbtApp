//
//  AppearenceHelper.swift
//  MBT
//
//  Created by Serkut Yegin on 18.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class AppearenceHelper {
    
    func setupAppearence() {
        UITextField.appearance(whenContainedInInstancesOf:[UISearchBar.self]).defaultTextAttributes = [NSAttributedStringKey.foregroundColor.rawValue: UIColor.white, NSAttributedStringKey.font.rawValue: UIFont(name: FontType.regular.fontName, size: 18)!]
        UILabel.appearance(whenContainedInInstancesOf:[UINavigationBar.self]).textColor = UIColor.white.withAlphaComponent(0.5)
        UILabel.appearance(whenContainedInInstancesOf:[UINavigationBar.self]).font = UIFont(name: FontType.regular.fontName, size: 15)!
        UIBarButtonItem.appearance().setTitleTextAttributes([.font: UIFont(name: FontType.regular.fontName, size: 18)!], for: .normal)
        UITabBarItem.appearance().setTitleTextAttributes([.font: UIFont(name: FontType.regular.fontName, size: 12)!,.foregroundColor:UIColor.white], for: .normal)
        UITabBarItem.appearance().setTitleTextAttributes([.font: UIFont(name: FontType.regular.fontName, size: 12)!,.foregroundColor:UIColor.mbtBlue], for: .selected)
        UITabBar.appearance().tintColor = UIColor.mbtBlue
        UITabBar.appearance().backgroundColor = UIColor.black
        UINavigationBar.appearance().backIndicatorImage = #imageLiteral(resourceName: "btnBack")
        UINavigationBar.appearance().backIndicatorTransitionMaskImage = #imageLiteral(resourceName: "btnBack")
    }

}
