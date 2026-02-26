//
//  TransportationOptionsInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationOptionsBusinessLogic
{
    func initializeView(request : TransportationOptions.Initialize.Request)
    func validateInputs(request : TransportationOptions.Validate.Request)
    func reloadPage(request : TransportationOptions.Reload.Request)
    func sendData(request : TransportationOptions.SendData.Request)
    
    var typeList : [MBTTransportationOptionsTypeList] { get }
    var companyLocationList : [MBTTransportationOptionsCompanyLocationList] { get }
}

protocol TransportationOptionsDataStore
{

}

class TransportationOptionsInteractor: TransportationOptionsBusinessLogic, TransportationOptionsDataStore
{
    var presenter: TransportationOptionsPresentationLogic?
    var worker = TransportationOptionsWorker()
    var typeList: [MBTTransportationOptionsTypeList] = []
    var companyLocationList: [MBTTransportationOptionsCompanyLocationList] = []
    
    func initializeView(request : TransportationOptions.Initialize.Request) {
        worker.getShuttleOptions { [weak self] (response) in
            self?.typeList = response?.typeList ?? []
            self?.companyLocationList = response?.companyLocationList ?? []
            self?.presenter?.presentInitializeViewResult(response: TransportationOptions.Initialize.Response(textFieldType: request.textFieldType, textFieldCompanyList: request.textFieldCompanyList))
        }
    }
    
    func validateInputs(request : TransportationOptions.Validate.Request) {
        presenter?.presentValidateInputs(response: TransportationOptions.Validate.Response())
    }
    
    func reloadPage(request : TransportationOptions.Reload.Request) {
        presenter?.presentReloadPageResult(response : TransportationOptions.Reload.Response())
    }
    
    func sendData(request : TransportationOptions.SendData.Request) {
        guard typeList.count > request.selectedRowType, companyLocationList.count > request.selectedRowCompanyList else { return }
        guard let typeId = typeList[request.selectedRowType].type else { return }
        let selectedCompanyLocationItem = companyLocationList[request.selectedRowCompanyList]
        guard let companyId = selectedCompanyLocationItem.id else { return }
        worker.getShuttleList(typeId, companyId: companyId) { [weak self] (response) in
            guard let response = response else { return }
            self?.presenter?.presentSendDataResult(response: TransportationOptions.SendData.Response(companyListItem:selectedCompanyLocationItem, response: response))
        }
    }
}
