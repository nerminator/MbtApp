//
//  TermOfUseViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum TermsOfUseType {
    case firstOpen, info
}

protocol TermOfUseDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : TermOfUse.Initialize.ViewModel)
    func displayValidateInputs(viewModel : TermOfUse.Validate.ViewModel)
    func displayReloadPageResult(viewModel : TermOfUse.Reload.ViewModel)
    func displaySendDataResult(viewModel : TermOfUse.SendData.ViewModel)
}

class TermOfUseViewController: MBTBaseViewController, TermOfUseDisplayLogic
{
    
    // MARK: IBOutlets
    @IBOutlet weak var btnAgree: BaseUIButtonDemi!
    @IBOutlet weak var webView: UIWebView!
    @IBOutlet weak var btnReject: BaseUIButtonDemi!
    @IBOutlet weak var constBottomHeight: NSLayoutConstraint!
    
    var termsOfUseType : TermsOfUseType = .firstOpen
    var htmlType : HtmlType = .termOfUse
    
    // MARK: VIP Protocols
    
    var interactor: TermOfUseBusinessLogic?
    var router: (NSObjectProtocol & TermOfUseRoutingLogic & TermOfUseDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        TermOfUseWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        TermOfUseWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        navigationItem.title = htmlType.title
        constBottomHeight.constant = termsOfUseType == .firstOpen ? 68 : 0
        webView.scrollView.showsVerticalScrollIndicator = true
        webView.scrollView.showsHorizontalScrollIndicator = true
        
        if #available(iOS 11.0, *) {
            webView.scrollView.contentInsetAdjustmentBehavior = .never
        }
        
        webView.scrollView.delegate = self
        loadHtml()
    }
    
}

extension TermOfUseViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : TermOfUse.Initialize.ViewModel) { }
    
    func displayValidateInputs(viewModel : TermOfUse.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : TermOfUse.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : TermOfUse.SendData.ViewModel) { }
}

extension TermOfUseViewController {
    
    override func shouldAddNavBarSeperator() -> Bool {
        return true
    }
    
    override func shouldAddInfoBarButton() -> Bool {
        return false
    }
    
    @IBAction func btnAgreeTapped(_ sender: UIButton) {
        MBTSingleton.shared.setTermsOfUseShown()
        //(UIApplication.shared.delegate as? AppDelegate)?.registerNotification()
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func btnRejectTapped(_ sender: UIButton) {
        showBasicAlert(with: "TXT_LOGIN_TERMS_WELCOME_REJECT_ALERT_TITLE".localized(), message: "TXT_LOGIN_TERMS_WELCOME_REJECT_ALERT_TEXT".localized())
    }
    
    fileprivate func loadHtml() {
        guard let html = htmlType.htmlString else { return }
        webView.loadHTMLString(html, baseURL: URL(string: "https://")!)
    }
}

extension TermOfUseViewController : UIWebViewDelegate, UIScrollViewDelegate {
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        showNavbarSeperator = scrollView.contentOffset.y > 0
        
        if(scrollView.contentOffset.y >= (scrollView.contentSize.height - scrollView.frame.size.height)){
            btnAgree.isEnabled = true
            btnAgree.backgroundColor = UIColor.mbtBlue
            btnReject.backgroundColor = UIColor.mbtBlue
            btnReject.isEnabled = true
        }
    }
    
    func webView(_ webView: UIWebView, shouldStartLoadWith request: URLRequest, navigationType: UIWebViewNavigationType) -> Bool {
        
        if navigationType == .linkClicked {
            if let event = request.url?.absoluteString, event.contains("event:") {
                let eventKey = event.replacingOccurrences(of: "event:", with: "")
                if let htmlType = HtmlType(rawValue: eventKey) {
                    router?.routeToTermOfUse(htmlType)
                }
            } else if let url = request.url, UIApplication.shared.canOpenURL(url) {
                UIApplication.shared.open(url, options: [:], completionHandler: nil)
                return false
            }
        }
        return true
    }
}
