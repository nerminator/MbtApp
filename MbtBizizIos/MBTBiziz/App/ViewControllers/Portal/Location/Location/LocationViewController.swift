//
//  LocationViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import AVFoundation
import MapKit

protocol LocationDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : Location.Initialize.ViewModel)
    func displayValidateInputs(viewModel : Location.Validate.ViewModel)
    func displayReloadPageResult(viewModel : Location.Reload.ViewModel)
    func displaySendDataResult(viewModel : Location.SendData.ViewModel)
}

class LocationViewController: MBTBaseViewController, LocationDisplayLogic
{
    @IBOutlet weak var constTableViewHeight: NSLayoutConstraint!
    @IBOutlet weak var constLocationDetailHeight: NSLayoutConstraint!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var btnLocationDetail: UIButton!
    @IBOutlet weak var lblLocationRooms: BaseUILabelRegular!
    @IBOutlet weak var lblLocationNumber: BaseUILabelDemi!
    @IBOutlet weak var lblLocationName: BaseUILabelRegular!
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var textFieldSearch: BaseUITextField!
    @IBOutlet weak var segmentLocation: UISegmentedControl!
    @IBOutlet weak var viewLocationDetail: UIView!
    
    @IBOutlet weak var viewLocationNumber: UIView!
    @IBOutlet weak var arrowDown: UIImageView!
    // MARK: VIP Protocols
    @IBOutlet weak var imgEmergency: UIImageView!
    
    var interactor: LocationBusinessLogic?
    var router: (NSObjectProtocol & LocationRoutingLogic & LocationDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        LocationWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        LocationWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        textFieldSearch.attributedPlaceholder = NSAttributedString.init(string: "TXT_LOCATIONS_LOCATIONS_PLACEHOLDER".localized(),
                                                                        attributes: [.foregroundColor : UIColor.white.withAlphaComponent(0.5),
                                                                                     .font: UIFont.mbtRegular(18)!])
        interactor?.initializeView(request: Location.Initialize.Request())
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        viewLocationDetail.roundCorners([.topLeft,.topRight], radius: 16)
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        coordinator.animate(alongsideTransition: nil) { (context) in
            self.viewLocationDetail.roundCorners([.topLeft,.topRight], radius: 16)
        }
    }
}

extension LocationViewController {
    
    //MARK: - IBActions
    @IBAction func segmentValueChanged(_ sender: UISegmentedControl) {
        interactor?.reloadPage(request: Location.Reload.Request(selectedSegment: sender.selectedSegmentIndex))
    }
    
    @IBAction func btnLocationDetailTapped(_ sender: UIButton) {
        if constLocationDetailHeight.constant == 82 {
            constLocationDetailHeight.constant = tableView.h + 105
        } else {
            constLocationDetailHeight.constant = 82
        }
        view.layoutIfNeededAnimated()
    }
}

extension LocationViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : Location.Initialize.ViewModel) {
        segmentLocation.removeAllSegments()
        interactor?.arrLocations.forEachEnumerated({ (index,item) in
            self.segmentLocation.insertSegment(withTitle: item.name, at: index, animated: false)
        })
        if segmentLocation.numberOfSegments > viewModel.initialSegmentIndex {
            segmentLocation.selectedSegmentIndex = viewModel.initialSegmentIndex
        }
        setupView(for: viewModel.selectedBuilding)
        reloadAnnotations(for: viewModel.selectedBuilding)
    }
    
    func displayValidateInputs(viewModel : Location.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : Location.Reload.ViewModel) {
        setupView(for: viewModel.selectedBuilding)
        reloadAnnotations(for: viewModel.selectedBuilding)
    }
    
    func displaySendDataResult(viewModel : Location.SendData.ViewModel) { }
    
    fileprivate func setupView(for building:MBTLocationBuildingList?) {
        lblLocationName.text = building?.name
        lblLocationNumber.text = building?.order?.toString ?? ""
        viewLocationNumber.isHidden = building!.isEmergency;
        imgEmergency.isHidden = !building!.isEmergency;
        
        let count = building?.meetingRoomList?.count ?? 0
        if(count > 0){
            arrowDown.isHidden = false
            lblLocationRooms.text = "\(count) " + "TXT_LOCATIONS_LOCATIONS_MEETING_ROOM".localized()
        } else{
            arrowDown.isHidden = true
            lblLocationRooms.text = ""
        }

        constTableViewHeight.constant = min(CGFloat(building?.meetingRoomList?.count ?? 0),5) * tableView.rowHeight
        if constLocationDetailHeight.constant != 82 {
            constLocationDetailHeight.constant = constTableViewHeight.constant + 105
        }
        tableView.reloadData()
        view.layoutIfNeeded()
    }
    
    fileprivate func reloadAnnotations(for building:MBTLocationBuildingList?) {
        mapView.removeAnnotations(mapView.annotations)
        interactor?.currentLocation?.buildingList?.forEach({ (building) in
            mapView.addAnnotation(building)
        })
        interactor?.currentLocation?.emergencyPoints?.forEach({ (building) in
            building.isEmergency = true
            mapView.addAnnotation(building)
        })
        
        if let currentBuilding = building {
            mapView.selectAnnotation(currentBuilding, animated: true)
            let region = MKCoordinateRegionMakeWithDistance(currentBuilding.coordinate, 750, 750)
            mapView.setRegion(region, animated: true)
        }
    }
}

extension LocationViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return interactor?.arrMeetingRooms.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MeetingRoomCell", for: indexPath)
        let label = cell.viewWithTag(10) as? UILabel
        label?.text = interactor?.arrMeetingRooms[indexPath.row].name
        return cell
    }
}

extension LocationViewController : UITextFieldDelegate {
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        router?.routeToLocationSearch(interactor?.arrLocations)
        return false
    }
}

extension LocationViewController : MKMapViewDelegate {
    
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
    
    func mapView(_ mapView: MKMapView, didSelect view: MKAnnotationView) {
        if let building = view.annotation as? MBTLocationBuildingList {
            interactor?.arrMeetingRooms = building.meetingRoomList ?? []
            setupView(for: building)
        }
    }
}
