//
//  SettingsTableCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 31.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol SettingsTableCellDelegate : class {
    func settingCell(_ settingCell : SettingsTableCell, switchValueChangedTo selected:Bool, for index: Int?)
}

class SettingsTableCell: UITableViewCell {

    weak var delegate : SettingsTableCellDelegate?
    @IBOutlet weak var lblTitle: BaseUILabelRegular!
    @IBOutlet weak var switchSetting: UISwitch!
    @IBOutlet weak var lblDescription: BaseUILabelRegular!
    fileprivate var index : Int?
    
    
    @IBAction func switchValueChanged(_ sender: UISwitch) {
        delegate?.settingCell(self, switchValueChangedTo: sender.isOn, for: index)
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    func setup(with setting : MBTSettingsNotificationSettingList?, index: Int) {
        self.index = index
        lblTitle.text = setting?.title
        lblDescription.text = setting?.subtitle
        switchSetting.isOn = setting?.isSelected ?? false
    }

}
