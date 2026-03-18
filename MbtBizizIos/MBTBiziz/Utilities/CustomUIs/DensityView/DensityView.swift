//
//  DensityView.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class DensityView: UIView {

    @IBOutlet weak var lblLocation: BaseUILabelRegular!
    @IBOutlet weak var lblDensity: BaseUILabelRegular!
    @IBOutlet var arrDensityIcons : [UIImageView]!
    @IBOutlet weak var textField: UITextField!
    @IBOutlet weak var btnOver: UIButton!
    fileprivate var currentDensity : MBTFoodMenuDensityInfo?
    fileprivate var densityInfo : [MBTFoodMenuDensityInfo]?
    fileprivate var pickerInfo : CommonPickerModel?
    
    @IBAction func btnOverTapped(_ sender: UIButton) {
        textField.becomeFirstResponder()
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        roundCorners([.topLeft,.topRight], radius: 16)
    }
    
    func setup(_ densityInfo : [MBTFoodMenuDensityInfo]?) {
        guard let densityInfo = densityInfo, densityInfo.count > 0 else { return }
        self.densityInfo = densityInfo
        self.currentDensity = densityInfo[0]
        setupView(for: self.currentDensity)
        
        pickerInfo = UIPickerView.commonPickerWithToolbar
        pickerInfo?.pickerView.delegate = self
        pickerInfo?.pickerView.dataSource = self
        pickerInfo?.doneButton.target = self
        pickerInfo?.doneButton.action = #selector(pickerFinished(_:))
        pickerInfo?.pickerView.reloadAllComponents()
        textField.inputView = pickerInfo?.pickerView
        textField.inputAccessoryView = pickerInfo?.toolBar
    }
    
    @objc func pickerFinished(_ sender : UIBarButtonItem) {
        textField.resignFirstResponder()
        if let picker = textField.inputPicker {
            self.currentDensity = densityInfo?[picker.selectedRow(inComponent: 0)]
            setupView(for: self.currentDensity)
        }
    }
    
    fileprivate func setupView(for currentInfo : MBTFoodMenuDensityInfo?) {
        lblLocation.text = currentInfo?.locationName
        lblDensity.text = currentInfo?.text
        arrDensityIcons.forEach({ $0.isHighlighted = false })
        for index in 0..<getNumberOfIconToHighlighted(from: currentInfo?.percent) {
            arrDensityIcons[index].isHighlighted = true
        }
    }
    
    fileprivate func getNumberOfIconToHighlighted(from percent:Int?) -> Int {
        guard let percent = percent, percent > 0 else { return 0 }
        if percent >= 100 { return 3 }
        let percentOffset = 3 * percent
        return percentOffset / 100
    }
}

extension DensityView : UIPickerViewDelegate, UIPickerViewDataSource {
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return densityInfo?.count ?? 0
    }
    
    func pickerView(_ pickerView: UIPickerView, attributedTitleForRow row: Int, forComponent component: Int) -> NSAttributedString? {
        return NSAttributedString(string: densityInfo?[row].locationName ?? "", attributes: [.foregroundColor: UIColor.white, .font: UIFont.mbtRegular(23)!])
    }
    
    func pickerView(_ pickerView: UIPickerView, rowHeightForComponent component: Int) -> CGFloat {
        return 35
    }
}
