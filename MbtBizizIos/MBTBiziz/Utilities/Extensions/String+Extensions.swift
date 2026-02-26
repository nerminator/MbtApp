//
//  String+Extensions.swift
//  MBT
//
//  Created by Serkut Yegin on 6.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

extension String {
    
    func isValidEmail() -> Bool {
        let emailFormat = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        let emailPredicate = NSPredicate(format:"SELF MATCHES %@", emailFormat)
        return emailPredicate.evaluate(with: self)
    }
}
