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
    @IBOutlet weak var textFieldPhoneNumber: TextInputField!
    @IBOutlet weak var constButtonBottom: NSLayoutConstraint!

    fileprivate let textInputController = TextInputController()
    fileprivate var phoneNumber : String {
        return textInputController.unformattedText() ?? ""
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
        textInputController.textInput = textFieldPhoneNumber // setting textInput
        textFieldPhoneNumber.inputDelegate = self
        let formatter = TextInputFormatter(textPattern: "###-###-##-##", prefix: "")
        formatter.allowedSymbolsRegex = "[0-9]"
        textInputController.formatter = formatter // setting formatter
        
        #if DEBUG
        textFieldPhoneNumber.content = "555-123-45-61"
        //textFieldPhoneNumber.content = "536-796-72-65"
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

extension WelcomeViewController: UITextInputDelegate {

    func selectionWillChange(_ textInput: UITextInput?) {

    }

    func selectionDidChange(_ textInput: UITextInput?) {

    }

    func textWillChange(_ textInput: UITextInput?) {

    }

    func textDidChange(_ textInput: UITextInput?) {

        interactor?.validateInputs(request: Welcome.Validate.Request(phoneNumber: phoneNumber))
    }
}
