//
//  LocationSearchInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LocationSearchBusinessLogic
{
    func initializeView(request : LocationSearch.Initialize.Request)
    func validateInputs(request : LocationSearch.Validate.Request)
    func reloadPage(request : LocationSearch.Reload.Request)
    func sendData(request : LocationSearch.SendData.Request)
    var filteredRoomList : [MBTLocationMeetingRoomList] { get set }
}

protocol LocationSearchDataStore
{
    var arrLocations : [MBTLocationItem] { get set }
}

class LocationSearchInteractor: LocationSearchBusinessLogic, LocationSearchDataStore
{
    var presenter: LocationSearchPresentationLogic?
    var worker = LocationSearchWorker()
    var arrLocations: [MBTLocationItem] = []
    fileprivate var arrRoomList : [MBTLocationMeetingRoomList] = []
    var filteredRoomList: [MBTLocationMeetingRoomList] = []
    
    func initializeView(request : LocationSearch.Initialize.Request) {
        arrLocations.forEach { (location) in
            location.buildingList?.forEach({ (building) in
                building.meetingRoomList?.forEach({ (room) in
                    room.buildingName = building.name
                    room.buildingId = building.id
                    room.locationId = location.id
                    arrRoomList.append(room)
                })
            })
        }
        presenter?.presentInitializeViewResult(response: LocationSearch.Initialize.Response())
    }
    
    func validateInputs(request : LocationSearch.Validate.Request) {
        guard filteredRoomList.count > request.index else { return }
        let meetingRoom = filteredRoomList[request.index]
        if let building = getBuilding(from: meetingRoom) {
            presenter?.presentValidateInputs(response: LocationSearch.Validate.Response(building: building, room: meetingRoom))
        }
    }
    
    func reloadPage(request : LocationSearch.Reload.Request) {
        if request.searchText.isEmpty {
            filteredRoomList = []
        } else {
            filteredRoomList = arrRoomList.filter({ $0.name?.localizedCaseInsensitiveContains(request.searchText) ?? false })
        }
        
        presenter?.presentReloadPageResult(response : LocationSearch.Reload.Response())
    }
    
    func sendData(request : LocationSearch.SendData.Request) {
        presenter?.presentSendDataResult(response : LocationSearch.SendData.Response())
    }
    
    fileprivate func getBuilding(from meetingRoom : MBTLocationMeetingRoomList) -> MBTLocationBuildingList? {
        guard let buildingId = meetingRoom.buildingId, let locationId = meetingRoom.locationId else { return nil }
        for location in arrLocations where (location.id != nil && location.id! == locationId) {
            if let buildings = location.buildingList {
                for building in buildings where (building.id != nil && building.id! == buildingId) {
                    return building
                }
            }
        }
        return nil
    }
  
}
