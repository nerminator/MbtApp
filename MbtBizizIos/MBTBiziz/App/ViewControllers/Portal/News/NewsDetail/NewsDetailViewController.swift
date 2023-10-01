//
//  NewsDetailViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

struct NewsDetailConstants {
    static var IMAGE_HEIGHT : CGFloat {
        return MBTConstants.Device.isIpad ? 300 : 240
    }
    
    static func getImageHeight(if hasImage : Bool?) -> CGFloat {
        if hasImage == nil || !hasImage! { return CGFloat.statusBarHeight + 44 }
        else { return IMAGE_HEIGHT }
    }
}

protocol NewsDetailDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : NewsDetail.Initialize.ViewModel)
    func displayValidateInputs(viewModel : NewsDetail.Validate.ViewModel)
    func displayReloadPageResult(viewModel : NewsDetail.Reload.ViewModel)
    func displaySendDataResult(viewModel : NewsDetail.SendData.ViewModel)
}

class NewsDetailViewController: MBTBaseViewController, NewsDetailDisplayLogic
{
    
    var fromPushNotification = false
    
    // MARK: IBOutlets
    @IBOutlet weak var imgViewBg: UIImageView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var constImageTop: NSLayoutConstraint!
    @IBOutlet weak var constImageHeight: NSLayoutConstraint!
    @IBOutlet weak var viewBlur: UIVisualEffectView!
    @IBOutlet weak var viewBlueHeight: NSLayoutConstraint!
    @IBOutlet weak var lblUrl: UILabel!
    @IBOutlet weak var constUrlBottom: NSLayoutConstraint!
    
    @IBAction func btnUrlTapped(_ sender: UIButton) {
        guard let urlStr = interactor?.newsDetail?.url, let url = URL.init(string: urlStr) else { return }
        UIApplication.shared.open(url, options: [:], completionHandler: nil)
    }
    
    fileprivate var topHeight : CGFloat {
        return CGFloat.statusBarHeight + (self.navigationController?.navigationBar.h ?? 0)
    }
    
    // MARK: VIP Protocols
    
    var interactor: NewsDetailBusinessLogic?
    var router: (NSObjectProtocol & NewsDetailRoutingLogic & NewsDetailDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        NewsDetailWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        NewsDetailWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        if #available(iOS 11.0, *) {
            tableView.contentInsetAdjustmentBehavior = .never
        }
        interactor?.initializeView(request: NewsDetail.Initialize.Request())
        if fromPushNotification {
            addCloseBarButtonItem()
        }
    }
}

extension NewsDetailViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : NewsDetail.Initialize.ViewModel) {
        self.navigationItem.title = viewModel.title
        interactor?.sendData(request: NewsDetail.SendData.Request())
    }
    
    func displayValidateInputs(viewModel : NewsDetail.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : NewsDetail.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : NewsDetail.SendData.ViewModel) {
        if let url = interactor?.newsDetail?.url, !url.isEmpty {
            lblUrl.text = url
            constUrlBottom.constant = 14
            view.layoutIfNeededAnimated()
        }
        imgViewBg.mbtSetImage(urlStr: interactor?.newsDetail?.image)
        tableView.reloadData()
    }
}

extension NewsDetailViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return (interactor?.newsDetailContent.count ?? 0) + 2
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            return tableView.dequeueReusableCell(withIdentifier: "EmptyCell", for: indexPath)
        } else if indexPath.row == 1 {
            let cell = tableView.dequeueReusableCell(withIdentifier: NewsDetailDateCell.className, for: indexPath) as! NewsDetailDateCell
            cell.lblDate.text = interactor?.newsDetail?.dateInfo
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: NewsDetailCell.className, for: indexPath) as! NewsDetailCell
            cell.setup(interactor?.newsDetailContent[indexPath.row - 2], index: indexPath.row - 2)
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 { return NewsDetailConstants.getImageHeight(if: interactor?.hasImage) }
        else if indexPath.row == 1 { return 70 }
        else { return UITableViewAutomaticDimension }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if NewsDetailConstants.getImageHeight(if: interactor?.hasImage) == 0 { return }
        handleScroll(for: scrollView.contentOffset.y + scrollView.contentInset.top)
    }
    
    func handleScroll(for offset:CGFloat) {
        handleBackgroundImage(for: offset)
        //handleImageViewDepth(for: offset)
        handleUrlView(for: offset)
    }
    
    func handleBackgroundImage(for offset:CGFloat) {
        if offset <= 0 {
            constImageTop.constant = 0
            constImageHeight.constant = NewsDetailConstants.getImageHeight(if: interactor?.hasImage) - offset/2
        } else if offset > 0 && offset < imgViewBg.h - topHeight {
            constImageTop.constant = -offset
            constImageHeight.constant = NewsDetailConstants.getImageHeight(if: interactor?.hasImage)
        } else {
            constImageTop.constant = -imgViewBg.h + topHeight
            constImageHeight.constant = NewsDetailConstants.getImageHeight(if: interactor?.hasImage)
        }
    }
    
    func handleImageViewDepth(for offset:CGFloat) {
        if offset <= imgViewBg.h - topHeight {
            if view.subviews.index(of: imgViewBg) != 0 {
                view.insertSubview(imgViewBg, belowSubview: tableView)
                viewBlur.isHidden = true
            }
        } else {
            if view.subviews.index(of: imgViewBg) == 0 {
                view.insertSubview(imgViewBg, aboveSubview: tableView)
                viewBlur.isHidden = false
                //viewBlueHeight.constant = topHeight
                view.layoutIfNeeded()
            }
        }
    }
    
    func handleUrlView(for offset:CGFloat) {
        guard let url = interactor?.newsDetail?.url, !url.isEmpty else { return }
        let bottomOffset = tableView.contentSize.height - tableView.bounds.height
        if offset > bottomOffset - 50 {
            if constUrlBottom.constant == 14 {
                constUrlBottom.constant = -100
                view.layoutIfNeededAnimated()
            }
        } else {
            if constUrlBottom.constant == -100 {
                constUrlBottom.constant = 14
                view.layoutIfNeededAnimated()
            }
        }
    }
}

