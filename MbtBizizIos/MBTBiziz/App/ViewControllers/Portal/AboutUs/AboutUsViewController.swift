//
//  AboutUsViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 22.10.2023.
//  Copyright © 2023 Daimler AG. All rights reserved.
//

import Foundation
import UIKit


class AboutUsViewController: MBTBaseViewController, UIScrollViewDelegate, UIWebViewDelegate
{
    
    // MARK: IBActions
    
    @IBOutlet weak var webView: UIWebView!
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
        
        webView.scrollView.delegate = self
        webView.delegate = self
        self.title = "TXT_ABOUTUS_TITLE".localized()
        loadHtml()
    }
    fileprivate func loadHtml() {
        let urlStr = "https://bizizapp.com/bizizPanel/public/storage/aboutus" + MBTConstants.Device.PreferredLanguageCode.uppercased() + ".html"
        
        let url = URL(string: urlStr)
        webView.loadRequest(URLRequest(url: url!))
    }
    
    func webView(_: UIWebView, shouldStartLoadWith: URLRequest, navigationType: UIWebView.NavigationType) -> Bool {
        if navigationType == UIWebView.NavigationType.linkClicked {
            UIApplication.shared.open(shouldStartLoadWith.url!, options: [:], completionHandler: nil)
            return false
        }
        return true
    }
    func webViewDidStartLoad(_ webView: UIWebView) {
        activityIndicator.startAnimating()
    }
    func webViewDidFinishLoad(_ webView: UIWebView) {
        activityIndicator.stopAnimating()
    }
    func webView(_ webView: UIWebView, didFailLoadWithError error: Error) {
        activityIndicator.stopAnimating()
    }
    @IBAction func socialMediaClicked(_ sender: Any) {
        let sc = SocialMediaViewController.fromStoryboard(.socialmedia)
        
        self.navigationController?.pushViewController(sc, animated: true)
    }
}


