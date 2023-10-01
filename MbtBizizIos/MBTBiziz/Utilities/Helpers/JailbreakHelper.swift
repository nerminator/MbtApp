//
//  JailbreakHelper.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.01.2020.
//  Copyright © 2020 Serkut Yegin. All rights reserved.
//

import UIKit

class JailbreakHelper: NSObject {

    static func isJailbroken() -> Bool {

        FileManager.default.fileExists(atPath: "")

        if TARGET_IPHONE_SIMULATOR != 1 {

            // Check 1 : existence of files that are common for jailbroken devices
            if FileManager.default.fileExists(atPath: "/Applications/Cydia.app")
                || FileManager.default.fileExists(atPath: "/Library/MobileSubstrate/MobileSubstrate.dylib")
                || FileManager.default.fileExists(atPath: "/bin/bash")
                || FileManager.default.fileExists(atPath: "/usr/sbin/sshd")
                || FileManager.default.fileExists(atPath: "/etc/apt")
                || FileManager.default.fileExists(atPath: "/private/var/lib/apt/")
                || UIApplication.shared.canOpenURL(URL(string:"cydia://package/com.example.package")!) {
                    return true
            }

            // Check 2 : Reading and writing in system directories (sandbox violation)
            let stringToWrite = "Jailbreak Test"
            do {
                try stringToWrite.write(toFile:"/private/JailbreakTest.txt", atomically:true, encoding:String.Encoding.utf8)
                //Device is jailbroken
                return true
            } catch {
                return false
            }
        } else {
            return false
        }
    }
}
