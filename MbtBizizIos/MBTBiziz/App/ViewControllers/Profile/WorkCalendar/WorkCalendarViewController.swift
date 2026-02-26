//
//  WorkCalendarViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol WorkCalendarDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : WorkCalendar.Initialize.ViewModel)
    func displayValidateInputs(viewModel : WorkCalendar.Validate.ViewModel)
    func displayReloadPageResult(viewModel : WorkCalendar.Reload.ViewModel)
    func displaySendDataResult(viewModel : WorkCalendar.SendData.ViewModel)
}

class WorkCalendarViewController: MBTBaseViewController, WorkCalendarDisplayLogic
{
    
    // MARK: IBOutlets
    
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var lblDateTitle: BaseUILabelDemi!
    @IBOutlet weak var btnNext: UIButton!
    @IBOutlet weak var btnPrevious: UIButton!
    
    // MARK: Properties
    fileprivate var calendarCell : CalendarTableCell?
    fileprivate var dateFormatter = DateFormatter()
    
    // MARK: VIP Protocols
    
    var interactor: WorkCalendarBusinessLogic?
    var router: (NSObjectProtocol & WorkCalendarRoutingLogic & WorkCalendarDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        WorkCalendarWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        WorkCalendarWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        dateFormatter.locale = .mbtLocale
        dateFormatter.dateFormat = "MMMM yyyy"
        lblDateTitle.text = dateFormatter.string(from: Date()).uppercased(with: .mbtLocale)
        interactor?.initializeView(request: WorkCalendar.Initialize.Request())
    }
    
}

extension WorkCalendarViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : WorkCalendar.Initialize.ViewModel) {
        calendarCell?.calendar.reloadData()
        reloadTable()
    }
    
    func displayValidateInputs(viewModel : WorkCalendar.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : WorkCalendar.Reload.ViewModel) {
        calendarCell?.calendar.reloadData()
        reloadTable()
    }
    
    func displaySendDataResult(viewModel : WorkCalendar.SendData.ViewModel) { }
}

extension WorkCalendarViewController {
    
    //MARK: - IBActions
    @IBAction func btnNextTapped(_ sender: UIButton) {
        calendarCell?.calendar.scrollToNextPage()
    }
    
    @IBAction func btnPreviousTapped(_ sender: UIButton) {
        calendarCell?.calendar.scrollToPreviousPage()
    }
    
}

extension WorkCalendarViewController : UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        if let _ = calendarCell?.calendar.selectedDate {
            return 2
        } else {
            return 1 + (interactor?.getDatesFromMonthList(calendarCell?.calendar.currentPage)?.count ?? 0)
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0 { return 1 }
        else {
            if let selectedDate = calendarCell?.calendar.selectedDate {
                return interactor?.getDateFromDateList(selectedDate)?.typeList?.count ?? 0
            } else {
                return 1
            }
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0 {
            if let cell = calendarCell { return cell }
            calendarCell = tableView.dequeueReusableCell(withIdentifier: "CalendarTableCell", for: indexPath) as? CalendarTableCell
            calendarCell?.delegate = self
            return calendarCell!
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "WorkTableCell", for: indexPath) as! WorkTableCell
            if let selectedDate = calendarCell?.calendar.selectedDate {
                let item = interactor?.getDateFromDateList(selectedDate)
                cell.setup(indexPath.row, dayText: item?.dayText, typeListItem: item?.typeList?[indexPath.row], isToday: isItemAtToday(item))
            } else {
                let items = interactor?.getDatesFromMonthList(calendarCell?.calendar.currentPage)
                let item = items?[indexPath.section - 1]
                cell.setup(indexPath.section - 1, dayText: item?.dayText, typeName: item?.typeName, typeColor: item?.typeColor, isToday: false)
            }
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return indexPath.section == 0 ? 400 : 88
    }
}

extension WorkCalendarViewController : CalendarTableCellDelegate {
    
    func calendarTable(calendarTable: CalendarTableCell, didChangeCurrentPageAt month: Date) {
        lblDateTitle.text = dateFormatter.string(from: month).uppercased(with: .mbtLocale)
        interactor?.reloadPage(request: WorkCalendar.Reload.Request(date: month))
        reloadTable()
    }
    
    func calendarTable(calendarTable: CalendarTableCell, itemFor date: Date) -> MBTWorkCalendarDayList? {
        return interactor?.getDateFromDateList(date)
    }
    
    func calendarTable(calendarTable: CalendarTableCell, didSelectItemAt date: Date) {
        reloadTable()
    }
    
    func calendarTable(calendarTable: CalendarTableCell, didDeselectItemAt date: Date) {
        reloadTable()
    }
}

fileprivate extension WorkCalendarViewController {
    
    func isItemAtToday(_ item : MBTWorkCalendarDayList?) -> Bool {
        guard let dateStr = item?.day else { return false }
        return interactor?.isItemAtToday(dateStr) ?? false
    }
    
    func itemIndex(_ indexPath: IndexPath, items: [MBTWorkCalendarDayList]?) -> Int {
        guard let items = items else { return 0 }
        var totalIndex = 0
        for index in 0..<indexPath.section - 1 {
            let item = items[index]
            totalIndex += item.typeList?.count ?? 0
        }
        return totalIndex + indexPath.row
    }
    
    func reloadTable() {
        tableView.reloadData()
        tableView.setContentOffset(CGPoint.zero, animated: true)
    }
}
