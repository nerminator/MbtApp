//
//  DriverInfoViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 14.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class DriverInfoViewController: UIViewController, PopupProtocol {

    @IBOutlet weak var lblPhoneNumber: BaseUILabelRegular!
    @IBOutlet weak var lblDriverName: BaseUILabelRegular!
    @IBOutlet weak var lblPlate: BaseUILabelDemi!
    @IBOutlet weak var lblLocation: BaseUILabelRegular!
    
    weak var popupContainer: PopupContainerViewController?
    var driverInfo : MBTTransportationListDriverInfo?
    var locationInfo : String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        lblLocation.text = locationInfo
        lblDriverName.text = driverInfo?.name
        lblPhoneNumber.text = driverInfo?.telephone
        lblPlate.text = driverInfo?.licensePlate
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func btnCallTapped(_ sender: UIButton) {
        guard let phoneNumber = driverInfo?.telephone else { return }
        if let url = URL(string: "tel://" + phoneNumber), UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url)
        }
    }
    
    @IBAction func btnDismissTapped(_ sender: UIButton) {
        popupContainer?.dismiss()
    }

}
