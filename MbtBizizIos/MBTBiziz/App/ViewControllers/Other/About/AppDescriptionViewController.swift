//
//  AppDescriptionViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 29.07.2024.
//  Copyright © 2024 Serkut Yegin. All rights reserved.
//

import UIKit

class AppDescriptionViewController: MBTBaseViewController
{
    @IBOutlet weak var appDescriptionTextView: UITextView!
    
    override func viewDidLoad() {
        appDescriptionTextView.text = MBTConstants.appDescriptionText
    }

}



