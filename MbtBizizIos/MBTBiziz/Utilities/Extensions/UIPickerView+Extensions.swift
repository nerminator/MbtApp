//
//  UIPickerView+Extensions.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

struct CommonPickerModel {
    var pickerView : UIPickerView
    var toolBar : UIToolbar
    var doneButton : UIBarButtonItem
}

struct CommonDatePickerModel {
    var datePicker : UIDatePicker
    var toolBar : UIToolbar
    var doneButton : UIBarButtonItem
}

extension UIPickerView {
    
    class var commonPickerWithToolbar : CommonPickerModel {
        let toolbar = UIToolbar()
        toolbar.barTintColor = UIColor.mbtToolbarBg
        toolbar.sizeToFit()
        let spaceButton = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        let doneButton = UIBarButtonItem(title: "TXT_COMMON_DONE".localized(), style: .plain, target: nil, action: nil)
        doneButton.tintColor = UIColor.white
        toolbar.setItems([spaceButton,doneButton], animated: false)
        let picker = UIPickerView()
        picker.backgroundColor = UIColor.mbtPickerBg
        picker.tintColor = UIColor.white
        return CommonPickerModel(pickerView: picker, toolBar: toolbar, doneButton: doneButton)
    }
}

extension UIDatePicker {
    
    class var commonPickerWithToolbar : CommonDatePickerModel {
        let datePicker = UIDatePicker()
        datePicker.datePickerMode = .date
        datePicker.locale = .mbtLocale
        let toolbar = UIToolbar()
        toolbar.sizeToFit()
        let spaceButton = UIBarButtonItem(barButtonSystemItem: .flexibleSpace, target: nil, action: nil)
        let doneButton = UIBarButtonItem(title: "TXT_COMMON_DONE".localized(), style: .plain, target: nil, action: nil)
        toolbar.setItems([spaceButton,doneButton], animated: false)
        return CommonDatePickerModel(datePicker: datePicker, toolBar: toolbar, doneButton: doneButton)
    }
}

extension UITextField {
    
    var inputPicker : UIPickerView? {
        return inputView as? UIPickerView
    }
}
