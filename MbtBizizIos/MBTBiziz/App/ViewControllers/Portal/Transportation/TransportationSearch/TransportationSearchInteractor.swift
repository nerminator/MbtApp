//
//  TransportationSearchInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

typealias SearchCompletionBlock = (_ selectedStopListItem : MBTTransportationListStopList?)->()

protocol TransportationSearchBusinessLogic
{
    func initializeView(request : TransportationSearch.Initialize.Request)
    func validateInputs(request : TransportationSearch.Validate.Request)
    func reloadPage(request : TransportationSearch.Reload.Request)
    func sendData(request : TransportationSearch.SendData.Request)
    
    var filteredStopList : [MBTTransportationListStopList] { get set }
    var previousSearchItems : [MBTTransportationSearchItem] { get set }
}

protocol TransportationSearchDataStore
{
    var shuttleList : [MBTTransportationListShuttleList]? { get set }
    var searchCompletionBlock : SearchCompletionBlock? { get set }
}

class TransportationSearchInteractor: TransportationSearchBusinessLogic, TransportationSearchDataStore
{
    var searchCompletionBlock: SearchCompletionBlock?
    
    var presenter: TransportationSearchPresentationLogic?
    var worker = TransportationSearchWorker()
    var shuttleList: [MBTTransportationListShuttleList]?
    fileprivate var stopList : [MBTTransportationListStopList] = []
    var filteredStopList: [MBTTransportationListStopList] = []
    var previousSearchItems: [MBTTransportationSearchItem] = []
    
    func initializeView(request : TransportationSearch.Initialize.Request) {
        stopList = []
        shuttleList?.forEach({ (shuttleList) in
            shuttleList.stopList?.forEach({ (stopListItem) in
                var item = stopListItem
                self.stopList.append(item.appendShuttleInfo(shuttleItem: shuttleList))
            })
        })
        filteredStopList.append(contentsOf: stopList)
        previousSearchItems = worker.getPreviousSearchItems()
        presenter?.presentInitializeViewResult(response: TransportationSearch.Initialize.Response())
    }
    
    func validateInputs(request : TransportationSearch.Validate.Request) {
        let selectedSearchItem = MBTTransportationSearchItem.init(from: filteredStopList[request.index])
        if !previousSearchItems.hasItem(selectedSearchItem) {
            previousSearchItems.append(selectedSearchItem)
            previousSearchItems.save()
        }
        presenter?.presentValidateInputs(response: TransportationSearch.Validate.Response())
    }
    
    func reloadPage(request : TransportationSearch.Reload.Request) {
        if request.searchText.isEmpty {
            filteredStopList = []
            filteredStopList.append(contentsOf: stopList)
        } else {
            filteredStopList = stopList.filter({  ($0.name?.localizedCaseInsensitiveContains(request.searchText) ?? false) })
        }
        presenter?.presentReloadPageResult(response : TransportationSearch.Reload.Response())
    }
    
    func sendData(request : TransportationSearch.SendData.Request) {
        presenter?.presentSendDataResult(response : TransportationSearch.SendData.Response())
    }
  
}
