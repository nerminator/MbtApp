//
//  WelcomeViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import AnyFormatKit
import Foundation
import UIKit

protocol WelcomeDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : Welcome.Initialize.ViewModel)
    func displayValidateInputs(viewModel : Welcome.Validate.ViewModel)
    func displayReloadPageResult(viewModel : Welcome.Reload.ViewModel)
    func displaySendDataResult(viewModel : Welcome.SendData.ViewModel)
}

class WelcomeViewController: MBTBaseViewController, WelcomeDisplayLogic
{
    
    @IBOutlet weak var btnLogin: BaseUIButtonDemi!
    @IBOutlet weak var textFieldPhoneNumber: UITextField!
    @IBOutlet weak var constButtonBottom: NSLayoutConstraint!

    fileprivate let textFieldController = TextFieldInputController()
    fileprivate let phoneFormatter = DefaultTextInputFormatter(textPattern: "###-###-##-##")
    fileprivate var phoneNumber : String {
        return phoneFormatter.unformat(textFieldPhoneNumber?.text) ?? ""
    }
    
    // MARK: VIP Protocols
    
    var interactor: WelcomeBusinessLogic?
    var router: (NSObjectProtocol & WelcomeRoutingLogic & WelcomeDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        WelcomeWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        WelcomeWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        
        textFieldPhoneNumber.font = UIFont.mbtRegular(18)
        textFieldController.formatter = phoneFormatter
        textFieldPhoneNumber.delegate = textFieldController
        
        #if DEBUG
        textFieldPhoneNumber.text = phoneFormatter.format("5551234561")
        //textFieldPhoneNumber.text = phoneFormatter.format("5367967265")
        #endif

        interactor?.reloadPage(request: Welcome.Reload.Request())
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        textFieldPhoneNumber.becomeFirstResponder()
    }
    
    @IBAction func textInputDidChange(_ sender: Any) {

        interactor?.validateInputs(request: Welcome.Validate.Request(phoneNumber: phoneNumber))
    }
}

extension WelcomeViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : Welcome.Initialize.ViewModel) { }
    
    func displayValidateInputs(viewModel : Welcome.Validate.ViewModel) {
        btnLogin.isEnabled = viewModel.isValid
    }
    
    func displayReloadPageResult(viewModel : Welcome.Reload.ViewModel) {

    }
    
    func displaySendDataResult(viewModel : Welcome.SendData.ViewModel) {
        if viewModel.isSuccess {
            router?.routeToActivation(phoneNumber)
        }
    }
}

extension WelcomeViewController {
    
    override func shouldListenKeyboardNotification() -> Bool {
        return true
    }
    
    override func keyboardAnimation(willShow: Bool, endFrame: CGRect) {
        constButtonBottom.constant = willShow ? endFrame.h + 20 : 20
    }
    
    @IBAction func btnLoginTapped(_ sender: UIButton) {
        interactor?.sendData(request: Welcome.SendData.Request(phoneNumber: phoneNumber))
    }


}


