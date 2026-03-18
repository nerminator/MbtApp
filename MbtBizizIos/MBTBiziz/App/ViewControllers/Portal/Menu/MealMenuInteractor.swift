//
//  MealMenuInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol MealMenuBusinessLogic
{
    func initializeView(request : MealMenu.Initialize.Request)
    func validateInputs(request : MealMenu.Validate.Request)
    func reloadPage(request : MealMenu.Reload.Request)
    func sendData(request : MealMenu.SendData.Request)
    
    var foodInfo: MBTFoodMenuFoodInfo? { get set }
    var shuttleInfo: MBTFoodMenuShuttleInfo? { get set }
    var densityInfo: [MBTFoodMenuDensityInfo]? { get set }
}

protocol MealMenuDataStore
{
    
}

class MealMenuInteractor: MealMenuBusinessLogic, MealMenuDataStore
{
    var presenter: MealMenuPresentationLogic?
    var worker = MealMenuWorker()
    var foodInfo: MBTFoodMenuFoodInfo?
    var shuttleInfo: MBTFoodMenuShuttleInfo?
    var densityInfo: [MBTFoodMenuDensityInfo]?
    
    func initializeView(request : MealMenu.Initialize.Request) {
        if let foodInfo = request.additionalInfo[MBTConstants.UserDefined.FoodListKey] as? MBTFoodMenuFoodInfo {
            self.foodInfo = foodInfo
        }
        if let shuttleInfo = request.additionalInfo[MBTConstants.UserDefined.ShuttleListKey] as? MBTFoodMenuShuttleInfo {
            self.shuttleInfo = shuttleInfo
        }
        if let densityInfo = request.additionalInfo[MBTConstants.UserDefined.DensityListKey] as? [MBTFoodMenuDensityInfo] {
            self.densityInfo = densityInfo
        }
        let densityHeight = request.additionalInfo[MBTConstants.UserDefined.DensityBottomInset] as? CGFloat
        presenter?.presentInitializeViewResult(response: MealMenu.Initialize.Response(densityInfo: densityInfo, densityHeight: densityHeight ?? 0))
    }
    
    func validateInputs(request : MealMenu.Validate.Request) {
        presenter?.presentValidateInputs(response: MealMenu.Validate.Response())
    }
    
    func reloadPage(request : MealMenu.Reload.Request) {
        presenter?.presentReloadPageResult(response : MealMenu.Reload.Response())
    }
    
    func sendData(request : MealMenu.SendData.Request) {
        presenter?.presentSendDataResult(response : MealMenu.SendData.Response())
    }
  
}
