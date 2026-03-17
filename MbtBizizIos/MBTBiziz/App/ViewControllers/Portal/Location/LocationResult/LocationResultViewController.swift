//
//  LocationResultViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import MapKit

protocol LocationResultDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : LocationResult.Initialize.ViewModel)
    func displayValidateInputs(viewModel : LocationResult.Validate.ViewModel)
    func displayReloadPageResult(viewModel : LocationResult.Reload.ViewModel)
    func displaySendDataResult(viewModel : LocationResult.SendData.ViewModel)
}

class LocationResultViewController: MBTBaseViewController, LocationResultDisplayLogic
{
    @IBOutlet weak var constTableViewHeight: NSLayoutConstraint!
    @IBOutlet weak var constLocationDetailHeight: NSLayoutConstraint!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var btnLocationDetail: UIButton!
    @IBOutlet weak var lblLocationRooms: BaseUILabelRegular!
    @IBOutlet weak var lblLocationNumber: BaseUILabelDemi!
    @IBOutlet weak var lblLocationName: BaseUILabelRegular!
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var viewLocationDetail: UIView!
    
    @IBOutlet weak var viewLocationNumber: UIView!
    // MARK: VIP Protocols
    
    var interactor: LocationResultBusinessLogic?
    var router: (NSObjectProtocol & LocationResultRoutingLogic & LocationResultDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        LocationResultWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        LocationResultWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        interactor?.initializeView(request: LocationResult.Initialize.Request())
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        viewLocationDetail.roundCorners([.topLeft,.topRight], radius: 16)
        btnLocationDetailTapped(btnLocationDetail)
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        coordinator.animate(alongsideTransition: nil) { (context) in
            self.viewLocationDetail.roundCorners([.topLeft,.topRight], radius: 16)
        }
    }
}

extension LocationResultViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : LocationResult.Initialize.ViewModel) {
        setupView(for: viewModel.selectedBuilding, selectedRoomIndex: viewModel.selectedRoomIndex)
        reloadAnnotations(for: viewModel.selectedBuilding)
    }
    
    func displayValidateInputs(viewModel : LocationResult.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : LocationResult.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : LocationResult.SendData.ViewModel) { }
    
    fileprivate func setupView(for building:MBTLocationBuildingList?, selectedRoomIndex:Int? = nil) {
        self.navigationItem.prompt = interactor?.meetingRoom?.name
        lblLocationName.text = building?.name
        viewLocationNumber.isHidden = building!.isEmergency;
        lblLocationNumber.text = building?.order?.toString ?? ""
        lblLocationRooms.text = "\(building?.meetingRoomList?.count ?? 0) " + "TXT_LOCATIONS_LOCATIONS_MEETING_ROOM".localized()
        constTableViewHeight.constant = min(CGFloat(building?.meetingRoomList?.count ?? 0),5) * tableView.rowHeight
        if constLocationDetailHeight.constant != 82 {
            constLocationDetailHeight.constant = constTableViewHeight.constant + 105
        }
        tableView.reloadData()
        view.layoutIfNeeded()
        if let selectedRoomIndex = selectedRoomIndex {
            Timer.runThisAfterDelay(seconds: 0.5) { [weak self] in
                self?.tableView.scrollToRow(at: IndexPath.init(row: selectedRoomIndex, section: 0), at: .middle, animated: true)
            }
        }
        
    }
    
    fileprivate func reloadAnnotations(for building:MBTLocationBuildingList?) {
        mapView.removeAnnotations(mapView.annotations)
        
        
        if let currentBuilding = building {
            mapView.addAnnotation(currentBuilding)
            mapView.selectAnnotation(currentBuilding, animated: true)
            let region = MKCoordinateRegionMakeWithDistance(currentBuilding.coordinate, 750, 750)
            mapView.setRegion(region, animated: true)
        }
    }
}

extension LocationResultViewController {
    
    //MARK: - IBActions
    @IBAction func btnLocationDetailTapped(_ sender: UIButton) {
        if constLocationDetailHeight.constant == 82 {
            constLocationDetailHeight.constant = tableView.h + 105
        } else {
            constLocationDetailHeight.constant = 82
        }
        view.layoutIfNeededAnimated()
    }
}

extension LocationResultViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return interactor?.arrMeetingRooms.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MeetingRoomCell", for: indexPath)
        let label = cell.viewWithTag(10) as? UILabel
        let meetingRoom = interactor?.arrMeetingRooms[indexPath.row]
        label?.text = meetingRoom?.name
        if let meetingRoomId = meetingRoom?.id, meetingRoomId == interactor?.meetingRoom?.id {
            label?.textColor = UIColor.mbtBlue
        } else {
            label?.textColor = UIColor.white
        }
        return cell
    }
}

extension LocationResultViewController : MKMapViewDelegate {
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        if annotation is MKUserLocation { return nil }
        
        guard let building = annotation as? MBTLocationBuildingList else { return nil }
        let kAnnotationIdentifier = building.isEmergency ? "EmergencyAnnotationIdentifier" : "AnnotationIdentifier"
        var annotationView:MKAnnotationView?
       
        annotationView = mapView.dequeueReusableAnnotationView(withIdentifier: kAnnotationIdentifier)
        if annotationView == nil {
           if (building.isEmergency){
               annotationView = EmergencyAnnotationView(annotation: annotation, reuseIdentifier: kAnnotationIdentifier)
               annotationView?.frame = CGRect.init(x: 0, y: 0, w: 32, h: 40)
           } else {
               annotationView = CustomAnnotationView(annotation: annotation, reuseIdentifier: kAnnotationIdentifier)
               annotationView?.frame = CGRect.init(x: 0, y: 0, w: 40, h: 40)
           }
           
        } else {
           annotationView!.annotation = annotation
        }
        
        return annotationView
    }
    
    func mapView(_ mapView: MKMapView, didDeselect view: MKAnnotationView) {
        if mapView.selectedAnnotations.count == 0, let annotation = view.annotation {
            mapView.selectAnnotation(annotation, animated: false)
        }
    }
}
