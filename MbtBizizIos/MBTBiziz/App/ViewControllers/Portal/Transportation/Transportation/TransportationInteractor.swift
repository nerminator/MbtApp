//
//  TransportationInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationBusinessLogic
{
    func initializeView(request : Transportation.Initialize.Request)
    func validateInputs(request : Transportation.Validate.Request)
    func reloadPage(request : Transportation.Reload.Request)
    func sendData(request : Transportation.SendData.Request)
    func switchLocations(request : Transportation.SwitchLocations.Request)
    func becomeFirstResponder(request : Transportation.BecomeFirstResponder.Request)
    func clearTextField(request : Transportation.ClearTextField.Request)
    func makeSearch(request : Transportation.SearchResult.Request)
    
    var isArrival : Bool { get }
    var searchedStopItem : MBTTransportationListStopList? { get }
}

protocol TransportationDataStore
{
    var transportationList : MBTTransportationListResponse? { get set }
    var selectedCompanyListItem : MBTTransportationOptionsCompanyLocationList? { get set }
}

class TransportationInteractor: TransportationBusinessLogic, TransportationDataStore
{
    var presenter: TransportationPresentationLogic?
    var worker = TransportationWorker()
    var transportationList: MBTTransportationListResponse?
    var selectedCompanyListItem: MBTTransportationOptionsCompanyLocationList?
    
    var isArrival = true
    var searchedStopItem : MBTTransportationListStopList?
    fileprivate var tabItems : [String] = []
    fileprivate var selectedTabItemIndex = 0
    
    
    func initializeView(request : Transportation.Initialize.Request) {
        tabItems = []
        currentCompanyItems.forEach { (companyList) in
            tabItems.append(companyList.name ?? "")
        }
        presenter?.presentInitializeViewResult(response: Transportation.Initialize.Response(tabItems: tabItems, shuttleList: currentShuttleList, arrivalText: selectedCompanyListItem?.name))
    }
    
    func validateInputs(request : Transportation.Validate.Request) {
        presenter?.presentValidateInputs(response: Transportation.Validate.Response())
    }
    
    func reloadPage(request : Transportation.Reload.Request) {
        selectedTabItemIndex = request.selectedTabIndex
        presenter?.presentReloadPageResult(response: Transportation.Reload.Response(shuttleList: currentShuttleList))
    }
    
    func sendData(request : Transportation.SendData.Request) {
        presenter?.presentSendDataResult(response : Transportation.SendData.Response())
    }
    
    func switchLocations(request: Transportation.SwitchLocations.Request) {
        isArrival = !isArrival
        selectedTabItemIndex = 0
        tabItems = []
        currentCompanyItems.forEach { (companyList) in
            tabItems.append(companyList.name ?? "")
        }
        presenter?.presentSwitchLocationResult(response: Transportation.SwitchLocations.Response(tabItems: tabItems, shuttleList: currentShuttleList))
    }
    
    func becomeFirstResponder(request: Transportation.BecomeFirstResponder.Request) {
        presenter?.presentBecomeFirstResponderResult(response: Transportation.BecomeFirstResponder.Response(needToRouteSearch: isArrival != request.isArrivalField, shuttleList: currentCompleteShuttleList))
    }
    
    func makeSearch(request: Transportation.SearchResult.Request) {
        searchedStopItem = request.selectedSearchItem
        presenter?.presentMakeSearchResult(response: Transportation.SearchResult.Response(shuttleList: currentShuttleList, arrivalText: isArrival ? selectedCompanyListItem?.name : searchedStopItem?.name, departureText: isArrival ? searchedStopItem?.name : selectedCompanyListItem?.name))
    }
    
    func clearTextField(request: Transportation.ClearTextField.Request) {
        searchedStopItem = nil
        presenter?.presentClearTextFieldResult(response: Transportation.ClearTextField.Response(shuttleList: currentShuttleList, arrivalText: isArrival ? selectedCompanyListItem?.name : "", departureText: isArrival ? "" : selectedCompanyListItem?.name))
    }
    
    fileprivate var currentCompanyItems : [MBTTransportationListToCompanyList] {
        guard let transportationList = transportationList else { return [] }
        return (isArrival ? transportationList.toCompanyList : transportationList.fromCompanyList) ?? []
    }
    
    fileprivate var currentCompleteShuttleList : [MBTTransportationListShuttleList] {
        guard let transportationList = transportationList else { return [] }
        let itemList = (isArrival ? transportationList.toCompanyList : transportationList.fromCompanyList) ?? []
        if itemList.count > selectedTabItemIndex {
            return itemList[selectedTabItemIndex].shuttleList ?? []
        }
        return []
    }
    
    fileprivate var currentShuttleList : [MBTTransportationListShuttleList] {
        guard let transportationList = transportationList else { return [] }
        let itemList = (isArrival ? transportationList.toCompanyList : transportationList.fromCompanyList) ?? []
        if itemList.count > selectedTabItemIndex {
            let shuttleList = itemList[selectedTabItemIndex].shuttleList ?? []
            if let searchedStopItem = searchedStopItem {
                return shuttleList.filter({ (shuttleListItem) -> Bool in
                    return searchedStopItem.shuttleId == shuttleListItem.id
                })
            } else {
                return shuttleList
            }
        }
        return []
    }
}
