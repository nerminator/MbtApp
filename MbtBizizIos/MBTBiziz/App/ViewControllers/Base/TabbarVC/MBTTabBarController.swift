//
//  MBTTabBarController.swift
//  MBT
//
//  Created by Serkut Yegin on 2.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class MBTTabBarController: UITabBarController {

    internal final let tabbarDelegate = MBTTabBarControllerDelegate()

    var homeVC: HomeViewController {
        return (self.viewControllers![0] as! BaseNavCon).viewControllers[0] as! HomeViewController
    }

    var notificationsVC: NotificationsViewController {
        return (self.viewControllers![1] as! BaseNavCon).viewControllers[0] as! NotificationsViewController
    }

    var profileVC: ProfileViewController {
        return (self.viewControllers![2] as! BaseNavCon).viewControllers[0] as! ProfileViewController
    }

    var unreadedNotificationCount: Int = 0 {
        didSet {
            if let item = tabBar.items?[1] {
                item.badgeColor = UIColor.mbtBlue
                item.badgeValue = unreadedNotificationCount == 0 ? nil : "\(unreadedNotificationCount)"
                repositionBadge()
            }
            if selectedIndex == 1 && unreadedNotificationCount > 0 {
                notificationsVC.interactor?.updatePage(request: Notifications.Update.Request())
            }
        }
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        setupTabBarAppearance()
        configureNativeTabBarItems()
        self.delegate = tabbarDelegate
    }

    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        setupTabBarAppearance()
    }

    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
    }

    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        MBTNotificationManager.shared.activateCachedNotificationIfNeccesary()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    func repositionBadge() {
        for tabBarItem in self.tabBar.subviews {
            for badgeView in tabBarItem.subviews where NSStringFromClass(badgeView.classForCoder) == "_UIBadgeView" {
                badgeView.layer.transform = CATransform3DIdentity
                badgeView.layer.transform = CATransform3DMakeTranslation(-5.0, 1.0, 1.0)
            }
        }
    }

    private func setupTabBarAppearance() {
        tabBar.tintColor = UIColor.mbtBlue
        tabBar.unselectedItemTintColor = UIColor.white.withAlphaComponent(0.82)
        tabBar.backgroundColor = UIColor.black
        tabBar.barTintColor = UIColor.black
        tabBar.barStyle = .black
        tabBar.isTranslucent = false
        tabBar.itemPositioning = .fill

        // Single custom selection pill drawn via the native selectionIndicatorImage API.
        tabBar.selectionIndicatorImage = makeSelectionPillImage()

        let normalAttributes: [NSAttributedString.Key: Any] = [
            .font: UIFont(name: FontType.regular.fontName, size: 10)!,
            .foregroundColor: UIColor.white.withAlphaComponent(0.82)
        ]
        let selectedAttributes: [NSAttributedString.Key: Any] = [
            .font: UIFont(name: FontType.regular.fontName, size: 10)!,
            .foregroundColor: UIColor.mbtBlue
        ]

        let appearance = UITabBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = UIColor.black
        appearance.stackedItemPositioning = .fill
        // Suppress any appearance-layer selection tint so only our image shows.
        appearance.selectionIndicatorTintColor = .clear

        appearance.stackedLayoutAppearance.normal.iconColor = UIColor.white.withAlphaComponent(0.82)
        appearance.stackedLayoutAppearance.normal.titleTextAttributes = normalAttributes
        appearance.stackedLayoutAppearance.selected.iconColor = UIColor.mbtBlue
        appearance.stackedLayoutAppearance.selected.titleTextAttributes = selectedAttributes

        appearance.inlineLayoutAppearance.normal.iconColor = UIColor.white.withAlphaComponent(0.82)
        appearance.inlineLayoutAppearance.normal.titleTextAttributes = normalAttributes
        appearance.inlineLayoutAppearance.selected.iconColor = UIColor.mbtBlue
        appearance.inlineLayoutAppearance.selected.titleTextAttributes = selectedAttributes

        appearance.compactInlineLayoutAppearance.normal.iconColor = UIColor.white.withAlphaComponent(0.82)
        appearance.compactInlineLayoutAppearance.normal.titleTextAttributes = normalAttributes
        appearance.compactInlineLayoutAppearance.selected.iconColor = UIColor.mbtBlue
        appearance.compactInlineLayoutAppearance.selected.titleTextAttributes = selectedAttributes

        tabBar.standardAppearance = appearance
        if #available(iOS 15.0, *) {
            tabBar.scrollEdgeAppearance = appearance
        }
    }

    private func configureNativeTabBarItems() {
        guard let items = tabBar.items, items.count >= 3 else { return }
        let iconNames = ["icnPortalActive", "icnNotificationsActive", "icnProfileActive"]
        for (index, item) in items.enumerated() {
            guard iconNames.indices.contains(index),
                  let image = UIImage(named: iconNames[index]) else { continue }
            item.image = image.withRenderingMode(.alwaysTemplate)
            item.selectedImage = image.withRenderingMode(.alwaysTemplate)
        }
    }

    /// Renders a single dark-gray rounded pill sized to one tab-item slot.
    /// This image is used as the native selectionIndicatorImage so there is
    /// exactly one selection visual — no custom overlay subview required.
    private func makeSelectionPillImage() -> UIImage {
        let screenWidth = UIScreen.main.bounds.width
        let tabCount: CGFloat = 3
        let itemWidth = screenWidth / tabCount
        let barHeight: CGFloat = 56       // icon + label region above home indicator
        let pillW: CGFloat = 72
        let pillH: CGFloat = 38
        let size = CGSize(width: itemWidth, height: barHeight)
        let pillRect = CGRect(
            x: (itemWidth - pillW) / 2,
            y: (barHeight - pillH) / 2,
            width: pillW,
            height: pillH
        )
        return UIGraphicsImageRenderer(size: size).image { _ in
            UIColor(white: 0.22, alpha: 1.0).setFill()
            UIBezierPath(roundedRect: pillRect, cornerRadius: pillH / 2).fill()
        }.withRenderingMode(.alwaysOriginal)
    }
}
