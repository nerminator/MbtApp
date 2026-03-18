//
//  ActivationCodeViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 17.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol ActivationCodeDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : ActivationCode.Initialize.ViewModel)
    func displayValidateInputs(viewModel : ActivationCode.Validate.ViewModel)
    func displayReloadPageResult(viewModel : ActivationCode.Reload.ViewModel)
    func displaySendDataResult(viewModel : ActivationCode.SendData.ViewModel)
    
    func displayRemainingTimeResult(viewModel : ActivationCode.Timer.ViewModel)
}

class ActivationCodeViewController: MBTBaseViewController, ActivationCodeDisplayLogic
{
    
    // MARK: IBActions
    
    @IBOutlet weak var lblRemainingTime: BaseUILabelRegular!
    @IBOutlet weak var btnResend: BaseUIButtonDemi!
    @IBOutlet weak var btnLogin: BaseUIButtonDemi!
    @IBOutlet weak var constButtonBottom: NSLayoutConstraint!
    @IBOutlet var arrOtpFields : [OtpTextField]!
    
    fileprivate var otp : String {
        var otp :String = ""
        arrOtpFields.forEach { (field) in
            otp = otp + (field.text ?? "")
        }
        return otp
    }
    
    // MARK: VIP Protocols
    
    var interactor: ActivationCodeBusinessLogic?
    var router: (NSObjectProtocol & ActivationCodeRoutingLogic & ActivationCodeDataPassing)?

    // MARK: Object lifecycle
    
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        ActivationCodeWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        ActivationCodeWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        interactor?.startTimer(request: ActivationCode.Timer.Request())
        
        #if DEBUG
        arrOtpFields.forEachEnumerated { (index, field) in
            field.text = (index + 1).toString
        }
        btnLogin.isEnabled = true
        #endif
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        arrOtpFields[0].becomeFirstResponder()
    }
}

extension ActivationCodeViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : ActivationCode.Initialize.ViewModel) { }
    
    func displayValidateInputs(viewModel : ActivationCode.Validate.ViewModel) {
        btnLogin.isEnabled = viewModel.isValid
    }
    
    func displayReloadPageResult(viewModel : ActivationCode.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : ActivationCode.SendData.ViewModel) {

        if viewModel.isSuccess {
            NavigationHelper().showHomeScreen()
        } else if viewModel.isExpired {
            Timer.runThisAfterDelay(seconds: 1) { [weak self] in
                self?.navigationController?.popViewController(animated: true)
            }
        }
    }
    
    func displayRemainingTimeResult(viewModel: ActivationCode.Timer.ViewModel) {
        lblRemainingTime.text = viewModel.timeRemainingStr
        btnResend.isEnabled = viewModel.isResendButtonEnabled
    }
}

extension ActivationCodeViewController {
    
    override func shouldListenKeyboardNotification() -> Bool {
        return true
    }
    
    override func keyboardAnimation(willShow: Bool, endFrame: CGRect) {
        constButtonBottom.constant = willShow ? endFrame.h + 20 : 20
    }
    
    @IBAction func btnResendTapped(_ sender: UIButton) {
        interactor?.reloadPage(request: ActivationCode.Reload.Request())
    }
    
    @IBAction func btnLoginTapped(_ sender: UIButton) {
        interactor?.sendData(request: ActivationCode.SendData.Request(otp: otp.toInt() ?? 0))
    }
    
    @IBAction func otpTextDidChange(_ sender: OtpTextField) {
        interactor?.validateInputs(request: ActivationCode.Validate.Request(otp: otp))
        if let index = arrOtpFields.indexes(of: sender).first {
            if let text = sender.text, !text.isEmpty {
                if index != 3 {
                    arrOtpFields[index+1].becomeFirstResponder()
                }
            } else {
                if index != 0 {
                    arrOtpFields[index-1].becomeFirstResponder()
                }
            }
        }
    }
    
}
