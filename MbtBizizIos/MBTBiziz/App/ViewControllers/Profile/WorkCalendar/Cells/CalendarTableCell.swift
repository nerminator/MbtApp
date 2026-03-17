//
//  CalendarTableCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol CalendarTableCellDelegate : class {
    
    func calendarTable(calendarTable:CalendarTableCell, didChangeCurrentPageAt month:Date)
    func calendarTable(calendarTable:CalendarTableCell, didSelectItemAt date:Date)
    func calendarTable(calendarTable:CalendarTableCell, didDeselectItemAt date:Date)
    func calendarTable(calendarTable:CalendarTableCell, itemFor date:Date) -> MBTWorkCalendarDayList?
    
}

class CalendarTableCell: UITableViewCell {

    @IBOutlet weak var calendar : FSCalendar!
    weak var delegate : CalendarTableCellDelegate?
    
    override func awakeFromNib() {
        super.awakeFromNib()
        calendar.appearance.borderRadius = 0.1
        calendar.appearance.titleFont = UIFont.mbtRegular(18)
        calendar.appearance.weekdayFont = UIFont.mbtRegular(14)
        calendar.appearance.weekdayTextColor = UIColor.white.withAlphaComponent(0.5)
        calendar.allowsMultipleSelection = true
        calendar.locale = .mbtLocale
        calendar.clipsToBounds = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
}

extension CalendarTableCell: FSCalendarDelegate, FSCalendarDataSource, FSCalendarDelegateAppearance {
    
    func calendar(_ calendar: FSCalendar, numberOfEventsFor date: Date) -> Int {
        let item = delegate?.calendarTable(calendarTable: self, itemFor: date)
        return item?.typeList?.count ?? 0
    }
    
    func calendar(_ calendar: FSCalendar, appearance: FSCalendarAppearance, eventDefaultColorsFor date: Date) -> [UIColor]? {
        let item = delegate?.calendarTable(calendarTable: self, itemFor: date)
        return item?.typeList?.map({ (typeItem) -> UIColor in
            return UIColor.fromHexString(typeItem.typeColor)
        })
    }
    
    func calendar(_ calendar: FSCalendar, appearance: FSCalendarAppearance, eventSelectionColorsFor date: Date) -> [UIColor]? {
        let item = delegate?.calendarTable(calendarTable: self, itemFor: date)
        return item?.typeList?.map({ (typeItem) -> UIColor in
            return UIColor.fromHexString(typeItem.typeColor)
        })
    }
    
    func calendarCurrentPageDidChange(_ calendar: FSCalendar) {
        if let selectedDate = calendar.selectedDate {
            calendar.deselect(selectedDate)
        }
        delegate?.calendarTable(calendarTable: self, didChangeCurrentPageAt: calendar.currentPage)
    }
    
    func calendar(_ calendar: FSCalendar, shouldSelect date: Date, at monthPosition: FSCalendarMonthPosition) -> Bool {
        if let date = calendar.selectedDate {
            calendar.deselect(date)
        }
        return true
    }
    
    func calendar(_ calendar: FSCalendar, didSelect date: Date, at monthPosition: FSCalendarMonthPosition) {
        delegate?.calendarTable(calendarTable: self, didSelectItemAt: date)
    }
    
    func calendar(_ calendar: FSCalendar, didDeselect date: Date, at monthPosition: FSCalendarMonthPosition) {
        delegate?.calendarTable(calendarTable: self, didDeselectItemAt: date)
    }
    
}
