//
//  TechnicalSupportViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

class TechnicalSupportViewController: UIViewController, PopupProtocol {

    @IBOutlet weak var lblPhone1: BaseUILabelRegular!
    @IBOutlet weak var lblPhone2: BaseUILabelRegular!
    @IBOutlet weak var lblPhone3: BaseUILabelRegular!
    @IBOutlet weak var lblPhoneNumber1: BaseUILabelRegular!
    @IBOutlet weak var lblPhoneNumber2: BaseUILabelRegular!
    @IBOutlet weak var lblPhoneNumber3: BaseUILabelRegular!
    
    weak var popupContainer: PopupContainerViewController?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        if let phoneList = sharedPhoneList
        {
        
            if   phoneList.count > 0,
                 let labelPhone = phoneList[0].label, let phoneNumber = phoneList[0].phone
            {
                
                self.lblPhone1.text = labelPhone;
                self.lblPhoneNumber1.text = phoneNumber;
            }
            
            if   phoneList.count > 1,
                 let labelPhone = phoneList[1].label, let phoneNumber = phoneList[1].phone
            {
                
                self.lblPhone2.text = labelPhone;
                self.lblPhoneNumber2.text = phoneNumber;
            }
            
            if   phoneList.count > 2,
                 let labelPhone = phoneList[2].label, let phoneNumber = phoneList[2].phone
            {
                
                self.lblPhone3.text = labelPhone;
                self.lblPhoneNumber3.text = phoneNumber;
            }
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    @IBAction func btnCallTapped(_ sender: UIButton) {
        let callNumber = sharedCallPhone ?? MBTConstants.Constants.PhoneNumberTechnicalSupport
                
        if let url = URL(string: "tel://" + callNumber), UIApplication.shared.canOpenURL(url) {
            UIApplication.shared.open(url)
        }
    }
    
    @IBAction func btnDismissTapped(_ sender: UIButton) {
        popupContainer?.dismiss()
    }
    
}
