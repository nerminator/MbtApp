//
//  LocationResultInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LocationResultBusinessLogic
{
    func initializeView(request : LocationResult.Initialize.Request)
    func validateInputs(request : LocationResult.Validate.Request)
    func reloadPage(request : LocationResult.Reload.Request)
    func sendData(request : LocationResult.SendData.Request)
    
    var arrMeetingRooms : [MBTLocationMeetingRoomList] { get set }
    var meetingRoom : MBTLocationMeetingRoomList? { get set }
}

protocol LocationResultDataStore
{
    var selectedBuilding : MBTLocationBuildingList? { get set }
    var selectedRoom : MBTLocationMeetingRoomList? { get set }
}

class LocationResultInteractor: LocationResultBusinessLogic, LocationResultDataStore
{
    var presenter: LocationResultPresentationLogic?
    var worker = LocationResultWorker()
    var selectedRoom: MBTLocationMeetingRoomList?
    var selectedBuilding: MBTLocationBuildingList?
    var arrMeetingRooms: [MBTLocationMeetingRoomList] = []
    var meetingRoom: MBTLocationMeetingRoomList?
    
    func initializeView(request : LocationResult.Initialize.Request) {
        meetingRoom = selectedRoom
        arrMeetingRooms = selectedBuilding?.meetingRoomList ?? []
        var selectedRoomIndex : Int?
        for (index,room) in arrMeetingRooms.enumerated() {
            if let meetingRoomId = meetingRoom?.id, meetingRoomId == room.id {
                selectedRoomIndex = index
                break
            }
        }
        presenter?.presentInitializeViewResult(response: LocationResult.Initialize.Response(selectedBuilding: selectedBuilding, selectedRoomIndex: selectedRoomIndex))
    }
    
    func validateInputs(request : LocationResult.Validate.Request) {
        presenter?.presentValidateInputs(response: LocationResult.Validate.Response())
    }
    
    func reloadPage(request : LocationResult.Reload.Request) {
        presenter?.presentReloadPageResult(response : LocationResult.Reload.Response())
    }
    
    func sendData(request : LocationResult.SendData.Request) {
        presenter?.presentSendDataResult(response : LocationResult.SendData.Response())
    }
  
}
