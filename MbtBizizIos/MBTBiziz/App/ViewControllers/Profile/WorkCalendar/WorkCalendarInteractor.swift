//
//  WorkCalendarInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 16.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol WorkCalendarBusinessLogic
{
    func initializeView(request : WorkCalendar.Initialize.Request)
    func validateInputs(request : WorkCalendar.Validate.Request)
    func reloadPage(request : WorkCalendar.Reload.Request)
    func sendData(request : WorkCalendar.SendData.Request)
    
    func getDateFromDateList(_ date : Date?) -> MBTWorkCalendarDayList?
    func getDatesFromMonthList(_ date : Date?) -> [MBTWorkCalendarInitialDayList]?
    func isItemAtToday(_ dateStr : String) -> Bool
}

protocol WorkCalendarDataStore
{

}

class WorkCalendarInteractor: NSObject, WorkCalendarBusinessLogic, WorkCalendarDataStore
{
    var presenter: WorkCalendarPresentationLogic?
    var worker = WorkCalendarWorker()
    fileprivate var dateList: [String : MBTWorkCalendarDayList] = [:]
    fileprivate var monthList: [String : [MBTWorkCalendarDayList]] = [:]
    fileprivate var initialMonthList: [String : [MBTWorkCalendarInitialDayList]] = [:]
    fileprivate var wsRequestCancellable : MBTCancellable?
    fileprivate var waitingDate : Date?
    
    fileprivate lazy var dateFormatter : DateFormatter = {
       let formatter = DateFormatter()
        formatter.dateFormat = "dd.MM.yyyy"
        return formatter
    }()
    
    fileprivate lazy var monthFormatter : DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "MM.yyyy"
        return formatter
    }()
    
    func initializeView(request : WorkCalendar.Initialize.Request) {
        worker.getWorkCalendar(dateFormatter.string(from: Date())) { [weak self] (response) in
            self?.handleWorkCalendarResponse(response)
            self?.presenter?.presentInitializeViewResult(response: WorkCalendar.Initialize.Response())
        }
    }
    
    func validateInputs(request : WorkCalendar.Validate.Request) {
        presenter?.presentValidateInputs(response: WorkCalendar.Validate.Response())
    }
    
    func reloadPage(request : WorkCalendar.Reload.Request) {
        let dateStr = monthFormatter.string(from: request.date)
        if let _ = monthList[dateStr] {
            presenter?.presentReloadPageResult(response : WorkCalendar.Reload.Response())
            return
        }
        
        NSObject.cancelPreviousPerformRequests(withTarget: self, selector: #selector(getInfoForWaitingDate), object: nil)
        waitingDate = request.date
        self.perform(#selector(getInfoForWaitingDate), with: nil, afterDelay: 0.5)
    }
    
    func sendData(request : WorkCalendar.SendData.Request) {
        presenter?.presentSendDataResult(response : WorkCalendar.SendData.Response())
    }
    
    @objc func getInfoForWaitingDate() {
        wsRequestCancellable?.cancel()
        guard let date = waitingDate else { return }
        wsRequestCancellable = worker.getWorkCalendar(dateFormatter.string(from: date)) { [weak self] (response) in
            self?.wsRequestCancellable = nil
            self?.handleWorkCalendarResponse(response)
            self?.presenter?.presentReloadPageResult(response : WorkCalendar.Reload.Response())
        }
    }
    
    fileprivate func handleWorkCalendarResponse(_ response : MBTWorkCalendarResponse?) {
        guard let response = response else { return }
        guard let year = response.year, let month = response.month else { return }
        let monthStr = month < 10 ? "0\(month)" : "\(month)"
        let key = monthStr + "." + "\(year)"
        monthList[key] = response.dayList ?? []
        initialMonthList[key] = response.initialDayList ?? []
        response.dayList?.forEach({ (dayItem) in
            if let day = dayItem.day {
                self.dateList[day] = dayItem
            }
        })
    }
    
    func getDateFromDateList(_ date: Date?) -> MBTWorkCalendarDayList? {
        guard let date = date else { return nil }
        return dateList[dateFormatter.string(from: date)]
    }
    
    func getDatesFromMonthList(_ date: Date?) -> [MBTWorkCalendarInitialDayList]? {
        guard let date = date else { return nil }
        return initialMonthList[monthFormatter.string(from: date)]
    }
    
    func isItemAtToday(_ dateStr: String) -> Bool {
        return dateStr == dateFormatter.string(from: Date())
    }
}
