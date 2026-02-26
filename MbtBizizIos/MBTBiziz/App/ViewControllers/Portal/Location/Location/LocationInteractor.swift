//
//  LocationInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LocationBusinessLogic
{
    func initializeView(request : Location.Initialize.Request)
    func validateInputs(request : Location.Validate.Request)
    func reloadPage(request : Location.Reload.Request)
    func sendData(request : Location.SendData.Request)
    
    var arrLocations : [MBTLocationItem] { get set }
    var arrMeetingRooms : [MBTLocationMeetingRoomList] { get set }
    var currentLocation : MBTLocationItem? { get }
}

protocol LocationDataStore
{

}

class LocationInteractor: LocationBusinessLogic, LocationDataStore
{
    var presenter: LocationPresentationLogic?
    var worker = LocationWorker()
    var arrLocations: [MBTLocationItem] = []
    var arrMeetingRooms: [MBTLocationMeetingRoomList] = []
    var selectedLocationIndex : Int = 0
    
    func initializeView(request : Location.Initialize.Request) {
        worker.getLocationDetails { [weak self] (locations) in
            self?.arrLocations = locations ?? []
            let index = self?.setInitialSegmentIndex() ?? 0
            let currentBuilding = self?.firstBuilding
            self?.arrMeetingRooms = currentBuilding?.meetingRoomList ?? []
            self?.presenter?.presentInitializeViewResult(response: Location.Initialize.Response(selectedBuilding: currentBuilding, initialSegmentIndex : index))
        }
    }
    
    func validateInputs(request : Location.Validate.Request) {
        presenter?.presentValidateInputs(response: Location.Validate.Response())
    }
    
    func reloadPage(request : Location.Reload.Request) {
        selectedLocationIndex = request.selectedSegment
        let currentBuilding = self.firstBuilding
        self.arrMeetingRooms = currentBuilding?.meetingRoomList ?? []
        presenter?.presentReloadPageResult(response : Location.Reload.Response(selectedBuilding: currentBuilding))
    }
    
    func sendData(request : Location.SendData.Request) {
        presenter?.presentSendDataResult(response : Location.SendData.Response())
    }
    
    fileprivate func setInitialSegmentIndex() -> Int {
        for (index,location) in arrLocations.enumerated() {
            if let isDefault = location.isDefault, isDefault {
                selectedLocationIndex = index
                break
            }
        }
        return selectedLocationIndex
    }
    
    fileprivate var firstBuilding : MBTLocationBuildingList? {
        if arrLocations.count > selectedLocationIndex {
            let location = arrLocations[selectedLocationIndex]
            return location.buildingList?.first
        }
        return nil
    }
    
    var currentLocation: MBTLocationItem? {
        if arrLocations.count > selectedLocationIndex {
            return arrLocations[selectedLocationIndex]
        }
        return nil
    }
  
}
