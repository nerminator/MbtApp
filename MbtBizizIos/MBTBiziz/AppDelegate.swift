//
//  AppDelegate.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import EZSwiftExtensions
import PureLayout
import Marshal
import UserNotifications
import SwiftUI
import MSAL


func handleUncaughtException(_ exception: NSException) {
    let crashDetails = """
    Name: \(exception.name)
    Reason: \(exception.reason ?? "Unknown")
    Stack Trace: \(exception.callStackSymbols.joined(separator: "\n"))
    """

    CrashLoggerHelper.shared.logCrash(message: crashDetails) // Your crash logging logic
}



func sendCrashToServer(_ crash: [String: Any]) {
    guard let url = URL(string: "https://your-backend-url.com/api/crash") else { return }

    var request = URLRequest(url: url)
    request.httpMethod = "POST"
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")

    let body = try? JSONSerialization.data(withJSONObject: crash)
    request.httpBody = body

    URLSession.shared.dataTask(with: request).resume()
}

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?


    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        MBTLanguageManager.sharedManager.initialize()
        AppearenceHelper().setupAppearence()
        UNUserNotificationCenter.current().delegate = self
        
        //NSSetUncaughtExceptionHandler(handleUncaughtException)
        
        //if let crash = UserDefaults.standard.dictionary(forKey: "lastCrash") {
        //    CrashLoggerHelper.shared.logCrash(message: crash) // Your crash logging logic
        //    UserDefaults.standard.removeObject(forKey: "lastCrash")
        //}
        
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        NavigationHelper().rootVC.view.isHidden = true
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        NavigationHelper().rootVC.view.isHidden = false
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        guard !TokenManager.sharedManager.accessToken.isEmpty else { return }
        WSProvider.shared.wsRequest(.getNotificationBadgeCount) { (response : WSResponse<Int>) in
            if response.isSuccess { MBTNotificationManager.shared.unreadedNotificationCount = response.data ?? 0 }
        }
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
    
    func application(_ application: UIApplication, shouldAllowExtensionPointIdentifier extensionPointIdentifier: UIApplicationExtensionPointIdentifier) -> Bool {
        if extensionPointIdentifier == .keyboard {
            return false
        }
        return true
    }
    
    func registerNotification(_ application : UIApplication = UIApplication.shared) {
        let center  = UNUserNotificationCenter.current()
        
        center.getNotificationSettings { (settings) in
            if settings.authorizationStatus == .notDetermined {
                MBTNotificationManager.shared.showPushNotificationInfoAlert {
                    // set the type as sound or badge
                    center.requestAuthorization(options: [.sound,.alert,.badge]) { (granted, error) in
                        // Enable or disable features based on authorization
                    }
                    application.registerForRemoteNotifications()
                }
            }
        }
    }
    
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        
        return MSALPublicClientApplication.handleMSALResponse(url, sourceApplication: options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String)
    }
}

extension AppDelegate : UNUserNotificationCenterDelegate {
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        let tokenString = deviceToken.reduce("", {$0 + String(format: "%02X",    $1)})
        print("tokenString")
        print(tokenString)
        MBTSingleton.shared.deviceTokenForPush = tokenString
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        MBTSingleton.shared.deviceTokenForPush = nil
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter,  willPresent notification: UNNotification, withCompletionHandler   completionHandler: @escaping (_ options:   UNNotificationPresentationOptions) -> Void) {
        #if DEBUG
        print("Handle push from foreground")
        print("\(notification.request.content.userInfo)")
        #endif
        MBTNotificationManager.shared.handleNotificationReceivedPayload(MBTNotificationPayload.init(userInfo: notification.request.content.userInfo))
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        #if DEBUG
        print("Handle push from background or closed")
        // if you set a member variable in didReceiveRemoteNotification, you  will know if this is from closed or background
        print("\(response.notification.request.content.userInfo)")
        #endif
        
        MBTNotificationManager.shared.handleNotificationOpenedPayload(MBTNotificationPayload.init(userInfo: response.notification.request.content.userInfo))
        completionHandler()
    }
    
}

