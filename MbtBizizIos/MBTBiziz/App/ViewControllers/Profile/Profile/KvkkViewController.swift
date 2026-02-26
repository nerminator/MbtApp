//
//  KvkkViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 17.11.2025.
//  Copyright © 2025 Onion Tech. All rights reserved.
//

import Foundation
import UIKit
import WebKit


class KvkkViewController: MBTBaseViewController, UIScrollViewDelegate
{
    
    // MARK: IBActions
    
    @IBOutlet weak var webView: WKWebView!
    
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    // MARK: VIP Protocols
    
    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)

    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
     }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        webView.scrollView.showsVerticalScrollIndicator = true
        webView.scrollView.showsHorizontalScrollIndicator = true
        
        if #available(iOS 11.0, *) {
            webView.scrollView.contentInsetAdjustmentBehavior = .never
        }
        
        webView.isOpaque = false
        webView.backgroundColor = .clear
        webView.scrollView.backgroundColor = .clear
        
        webView.scrollView.delegate = self
        webView.navigationDelegate = self
        self.title = "TXT_KVKK_TITLE".localized()
        loadHtml()
    }
    fileprivate func loadHtml() {
        
        let ts = Int(Date().timeIntervalSince1970)
        let urlStr = "https://bizizapp.com/bizizPanel/public/kvkk.html?v=\(ts)"
        let url = URL(string: urlStr)
        webView.load(URLRequest(url: url!))
    }
    
    
    @IBAction func noApproveClicked(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    @IBAction func approveButtonClicked(_ sender: Any) {
        WSProvider.shared.wsRequest(.activateDigitalCard) { (_ response: MBTActivateBusinessCardResponse?) in
            
            if let digitalCardUrl = response?.digitalCardUrl, let link = URL(string: digitalCardUrl) {
                self.navigationController?.popViewController(animated: true)
                UIApplication.shared.open(link)
            }
            
        } failure: { error in
            self.showAlert(message: "Bağlantı Hatası!")
            
        }
    }
    
}

extension KvkkViewController: WKNavigationDelegate {

    func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        activityIndicator.startAnimating()
    }

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        activityIndicator.stopAnimating()
    }

    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        activityIndicator.stopAnimating()
    }

    func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
        // Also called for some load errors
        activityIndicator.stopAnimating()
    }
}


