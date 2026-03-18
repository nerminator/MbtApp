//
//  OtpTextField.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 18.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class OtpTextField: BaseUITextFieldLight {
    
    override func awakeFromNib() {
        super.awakeFromNib()
        backgroundColor = UIColor.white.withAlphaComponent(0.06)
        layer.borderWidth = 1
        layer.borderColor = UIColor.clear.cgColor
        tintColor = .clear
        keyboardType = .numberPad
        keyboardAppearance = .dark
        delegate = self
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        layer.cornerRadius = self.h/2
        layer.masksToBounds = true
    }

    @discardableResult
    override func becomeFirstResponder() -> Bool {
        layer.borderColor = UIColor.mbtBlue.cgColor
        backgroundColor = .clear
        return super.becomeFirstResponder()
    }
    
    @discardableResult
    override func resignFirstResponder() -> Bool {
        if let text = text, !text.isEmpty {
            layer.borderColor = UIColor.white.cgColor
            backgroundColor = .clear
        } else {
            layer.borderColor = UIColor.clear.cgColor
            backgroundColor = UIColor.white.withAlphaComponent(0.06)
        }
        return super.resignFirstResponder()
    }

}

extension OtpTextField : UITextFieldDelegate {
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        guard let text = textField.text else { return true }
        let allowedCharactersDecimal = CharacterSet.decimalDigits
        let characterSet = CharacterSet(charactersIn: string)
        let newLength = text.count + string.count - range.length
        return newLength <= 1 && allowedCharactersDecimal.isSuperset(of: characterSet)
    }
}
