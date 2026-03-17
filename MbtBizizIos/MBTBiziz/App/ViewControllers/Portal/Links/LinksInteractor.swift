//
//  LinksInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 19.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LinksBusinessLogic
{
   /* func initializeView(request : Home.Initialize.Request)
    func validateInputs(request : Home.Validate.Request)
    func reloadPage(request : Home.Reload.Request)
    func sendData(request : Home.SendData.Request)
    
    func getMealInfo(request: Home.MealInfo.Request)*/
}

protocol LinksDataStore
{

}

class LinksInteractor: LinksBusinessLogic, LinksDataStore
{
    var presenter: LinksPresentationLogic?
    var worker = LinksWorker()
   /*
    func initializeView(request : Home.Initialize.Request) {
        presenter?.presentInitializeViewResult(response: Home.Initialize.Response())
    }
    
    func validateInputs(request : Home.Validate.Request) {
        presenter?.presentValidateInputs(response: Home.Validate.Response())
    }
    
    func reloadPage(request : Home.Reload.Request) {
        presenter?.presentReloadPageResult(response : Home.Reload.Response())
    }
    
    func sendData(request : Home.SendData.Request) {
        presenter?.presentSendDataResult(response : Home.SendData.Response())
    }
    
    func getMealInfo(request: Home.MealInfo.Request) {
        worker.getMealInfo { [weak self] (response) in
            guard let response =  response else { return }
            self?.presenter?.presentGetMealInfoResult(response: Home.MealInfo.Response(mealInfo: response))
        }
    }
  */
}
