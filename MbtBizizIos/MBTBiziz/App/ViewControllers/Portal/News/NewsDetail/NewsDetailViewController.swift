//
//  NewsDetailViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import Kingfisher
import ImageSlideshow

struct NewsDetailConstants {
    static var IMAGE_HEIGHT : CGFloat {
        let width = UIScreen.main.bounds.width
        return width*0.7149
    }
    
    static func getImageHeight(if hasImage : Bool?) -> CGFloat {
        if hasImage == nil || !hasImage! { return  1 }
        else { return IMAGE_HEIGHT }
    }
}

protocol NewsDetailDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : NewsDetail.Initialize.ViewModel)
    func displayValidateInputs(viewModel : NewsDetail.Validate.ViewModel)
    func displayReloadPageResult(viewModel : NewsDetail.Reload.ViewModel)
    func displaySendDataResult(viewModel : NewsDetail.SendData.ViewModel)
    func displayGetUserCodeResult(response: MBTGetDiscountCodeResponse)
    func displayNoDiscountCodeResult()
}

class NewsDetailViewController: MBTBaseViewController, NewsDetailDisplayLogic, ImageSlideshowDelegate
{
    var fromPushNotification = false
    
    // MARK: IBOutlets
    @IBOutlet weak var imagesView: ImageSlideshow!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var viewBlur: UIVisualEffectView!
    @IBOutlet weak var lblUrl: UILabel!

    @IBOutlet weak var constUrlHeight: NSLayoutConstraint!
    @IBOutlet weak var constPdfHeight: NSLayoutConstraint!
    @IBOutlet weak var constImgHeight: NSLayoutConstraint!
    @IBOutlet weak var constImgTop: NSLayoutConstraint!
    @IBAction func btnUrlTapped(_ sender: UIButton) {
        guard let urlStr = interactor?.newsDetail?.url, let url = URL.init(string: urlStr) else { return }
        UIApplication.shared.open(url, options: [:], completionHandler: nil)
    }
    
    @IBOutlet weak var pdfScrollView: PdfScrollView!
    
    @IBOutlet weak var heightDiscountCodeView: NSLayoutConstraint!
    
    @IBOutlet weak var lblDiscountCode: UILabel!
    
    @IBOutlet weak var btnGetDiscountCode: BaseUIButton!
    
    @IBOutlet weak var lblCopy: BaseUILabelDemi!
    @IBOutlet weak var btnCopyDiscountCode: BaseUIButton!
    
    @IBOutlet weak var discountCodeView: UIView!
    
    @IBOutlet weak var lblTitleDiscountCode: BaseUILabelDemi!
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
  
    @IBAction func getDiscountCodeClicked(_ sender: Any) {
        interactor?.getUserDiscountCode(request: NewsDetail.GetUserDiscountCode.Request())
    }
    
    @IBAction func copyDiscountCodeClicked(_ sender: Any) {
        UIPasteboard.general.string = lblDiscountCode.text
        btnCopyDiscountCode.setImage(UIImage(named: "icnCheck"), for: .normal)
        lblCopy.text = "TXT_COPIED".localized()
        lblCopy.textColor =  UIColor.mbtBlue
    }
    
    // MARK: View lifecycle
  
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        pdfScrollView.flashScrollIndicators()
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        if #available(iOS 11.0, *) {
            tableView.contentInsetAdjustmentBehavior = .never
        }
        interactor?.initializeView(request: NewsDetail.Initialize.Request())
        if fromPushNotification {
            addCloseBarButtonItem()
        }
        let gestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(NewsDetailViewController.didTap))
          imagesView.addGestureRecognizer(gestureRecognizer)
        imagesView.zoomEnabled = true
        constImgHeight.constant = NewsDetailConstants.getImageHeight(if: interactor?.hasImage)
        self.view.layoutIfNeeded()
    }
    
    @objc func didTap() {
        imagesView.presentFullScreenController(from: self)
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
    
    @objc func pdfClicked(_ sender: UIButton){
        if let urlStr = sender.layer.name, let url: URL = URL(string: urlStr){
            UIApplication.shared.open(url)
        }
    }
        
    func displaySendDataResult(viewModel : NewsDetail.SendData.ViewModel) {
        if let url = interactor?.newsDetail?.url, !url.isEmpty {
            lblUrl.text = url
        } else {
            constUrlHeight.constant = 0
            view.layoutIfNeeded()
        }
        if let pdfs = interactor?.newsDetail?.pdfs, pdfs.count != 0 {
            if let pdfs=interactor?.newsDetail?.pdfs {
                var xPos =  CGFloat(0)
                for pdf in pdfs{
                    let pdfButton = PdfButton(x: xPos, y: 0, w: 90, h: 80, target: self, action: #selector(self.pdfClicked(_:)))
                    pdfButton.setTitle(pdf.name, for: UIControlState.normal)
                    pdfButton.setTitleColor(UIColor.lightGray, for: UIControlState.normal)
                    pdfButton.setImage(UIImage(named:"icnPdf"), for: UIControlState.normal)

                    // Data, url for pdf
                    pdfButton.layer.name = pdf.pdf
                    pdfButton.alignVertical(spacing:4)
                    
                    var width = pdf.name!.size(withAttributes: [.font: pdfButton.titleLabel!.font! as Any]).width
                    if width > 150 {width = 150}
                        
                    pdfButton.frame = CGRect(x: pdfButton.frame.x, y: pdfButton.frame.y, width: width, height: pdfButton.frame.height)
                    pdfScrollView.addSubview(pdfButton)
                    xPos += (width + 8)
                }
            }
            var contentRect = CGRectZero
            for view in self.pdfScrollView.subviews {
                contentRect = CGRectUnion(contentRect, view.frame)
            }
     
            pdfScrollView.contentSize  = contentRect.size
            pdfScrollView.showsHorizontalScrollIndicator = true
            pdfScrollView.indicatorStyle = UIScrollViewIndicatorStyle.white

            view.layoutIfNeededAnimated()
        } else {
            constPdfHeight.constant = 0
            view.layoutIfNeeded()
        }
        if let images = interactor?.newsDetail?.images {
            constImgHeight.constant = NewsDetailConstants.getImageHeight(if: interactor?.hasImage)
            let sources:[InputSource] = images.map {
                KingfisherSource(urlString: $0)! ;
            }
            imagesView.setImageInputs(sources);
        }

        if let newsType = interactor?.newsDetail?.type, newsType == NewsType.indirim.rawValue {
            if let discountCodeType = interactor?.newsDetail?.discountCodeType {
                
                if (discountCodeType == 0){
                    heightDiscountCodeView.constant = 0
                }
                if (discountCodeType == 1) { //One code for all
                    discountCodeView.isHidden = false
                    if let code = interactor?.newsDetail?.discountCodeAll {
                        lblDiscountCode.text = code
                    } else {
                        lblDiscountCode.isHidden = true
                        btnCopyDiscountCode.isHidden = true
                        lblCopy.isHidden = true
                        lblTitleDiscountCode.isHidden = true
                        
                        btnGetDiscountCode.isHidden = false
                        btnGetDiscountCode.isEnabled = false
                        btnGetDiscountCode.setTitleColor(.gray, for: .disabled)
                        btnGetDiscountCode.setTitle( "TXT_NO_DISCOUNT_CODE".localized(), for: .disabled)
                    }
                    
                } else if (discountCodeType == 2) { //One code for each
                    discountCodeView.isHidden = false
                    btnGetDiscountCode.isHidden = false
                    lblDiscountCode.isHidden = true
                    btnCopyDiscountCode.isHidden = true
                    lblCopy.isHidden = true
                    lblTitleDiscountCode.isHidden = true
                }
            }
                
        } else{
            heightDiscountCodeView.constant = 0
        }
       // imgViewBg.mbtSetImage(urlStr: interactor?.newsDetail?.image)
        tableView.reloadData()
    }
    
    func displayGetUserCodeResult(response : MBTGetDiscountCodeResponse){
        if let code = response.code{
            btnGetDiscountCode.isHidden = true
            lblDiscountCode.isHidden = false
            btnCopyDiscountCode.isHidden = false
            lblCopy.isHidden = false
            lblTitleDiscountCode.isHidden = false
            lblDiscountCode.text = code
        }
    }
    func displayNoDiscountCodeResult(){
        btnGetDiscountCode.isEnabled = false
        btnGetDiscountCode.setTitleColor(.gray, for: .disabled)
        btnGetDiscountCode.setTitle( "TXT_NO_DISCOUNT_CODE".localized(), for: .disabled)
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
        if indexPath.row == 0 {
            var a = NewsDetailConstants.getImageHeight(if: interactor?.hasImage)
            return a 
        }
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
        //handleUrlView(for: offset)
    }
    
    func handleBackgroundImage(for offset:CGFloat) {
        if offset <= 0 {
            constImgTop.constant = 0
            constImgHeight.constant = NewsDetailConstants.getImageHeight(if: interactor?.hasImage) - offset/2
        } else if offset > 0 && offset < imagesView.h - topHeight {
            constImgTop.constant = -offset
            constImgHeight.constant = NewsDetailConstants.getImageHeight(if: interactor?.hasImage)
        } else {
            constImgTop.constant = -imagesView.h + topHeight
            constImgHeight.constant = NewsDetailConstants.getImageHeight(if: interactor?.hasImage)
        }
    }
    
    func handleImageViewDepth(for offset:CGFloat) {
        if offset <= imagesView.h - topHeight {
            if view.subviews.index(of: imagesView) != 0 {
                view.insertSubview(imagesView, belowSubview: tableView)
                viewBlur.isHidden = true
            }
        } else {
            if view.subviews.index(of: imagesView) == 0 {
                view.insertSubview(imagesView, aboveSubview: tableView)
                viewBlur.isHidden = false
                view.layoutIfNeeded()
            }
        }
    }
    
   /* func handleUrlView(for offset:CGFloat) {
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
    }*/
}

extension NewsDetailViewController  {
    
}
class PdfScrollView : UIScrollView {
    override func touchesShouldCancel(in view: UIView) -> Bool {
        return true
    }
}
class PdfButton: BaseUIButton {
  func alignVertical(spacing: CGFloat = 6.0) {
    guard let imageSize = imageView?.image?.size,
      let text = titleLabel?.text,
      let font = titleLabel?.font
    else { return }

    titleEdgeInsets = UIEdgeInsets(
      top: 0,
      left: -imageSize.width,
      bottom: -(imageSize.height + spacing),
      right: 0.0
    )

    let titleSize = text.size(withAttributes: [.font: font])
    imageEdgeInsets = UIEdgeInsets(
      top: -(titleSize.height + spacing),
      left: 0.0,
      bottom: 0.0,
      right: -titleSize.width
    )

    let edgeOffset = abs(titleSize.height - imageSize.height) / 2.0
    contentEdgeInsets = UIEdgeInsets(
      top: edgeOffset,
      left: 0.0,
      bottom: edgeOffset,
      right: 0.0
    )
  }
}

