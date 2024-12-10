//
//  AboutViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.04.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol AboutDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : About.Initialize.ViewModel)
    func displayValidateInputs(viewModel : About.Validate.ViewModel)
    func displayReloadPageResult(viewModel : About.Reload.ViewModel)
    func displaySendDataResult(viewModel : About.SendData.ViewModel)
}

class AboutViewController: MBTBaseViewController, AboutDisplayLogic
{
    
    // MARK: IBActions
    
    @IBOutlet weak var tableView: UITableView!
    internal var descriptionCell : AboutDescriptionCell?
    internal let headerView = MBTTitleView.fromNib()
    fileprivate let arrVerifiers : [HtmlType] = [.appDescription, .termOfUse, .dataProtection, .foss, .thirParty, .legal, .appSupport]
    
    // MARK: VIP Protocols
    
    var interactor: AboutBusinessLogic?
    var router: (NSObjectProtocol & AboutRoutingLogic & AboutDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        AboutWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        AboutWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        headerView.lblTitle.text = "TXT_ABOUT_HEADER".localized()
        tableView.rowHeight = UITableViewAutomaticDimension
        tableView.estimatedRowHeight = 200
    }
    @IBAction func appFeedbackClicked(_ sender: Any) {
        let af = AppFeedbackViewController.fromStoryboard(.appfeedback)
        self.navigationController?.pushViewController(af, animated: true)
    }
}

extension AboutViewController {
    
    override func shouldAddInfoBarButton() -> Bool {
        return false
    }
    
    override func shouldAddNavBarSeperator() -> Bool {
        return true
    }
}

extension AboutViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : About.Initialize.ViewModel) { }
    
    func displayValidateInputs(viewModel : About.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : About.Reload.ViewModel) { }
    
    func displaySendDataResult(viewModel : About.SendData.ViewModel) { }
    
    
}

extension AboutViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrVerifiers.count + 3
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return indexPath.row == 1 ? UITableViewAutomaticDimension : indexPath.row == 0 ? 48 : 64
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            return tableView.dequeueReusableCell(withIdentifier: "AboutHeaderCell", for: indexPath)
        } else if indexPath.row == 1 {
            if descriptionCell == nil {
                descriptionCell = tableView.dequeueReusableCell(withIdentifier: AboutDescriptionCell.className, for: indexPath) as? AboutDescriptionCell
                descriptionCell?.aboutTextView.text = MBTConstants.aboutText
            }
            return descriptionCell!
        } else if indexPath.row == arrVerifiers.count + 2 {
            return tableView.dequeueReusableCell(withIdentifier: AboutVersionCell.className, for: indexPath)
        } else {
            let verifier = tableView.dequeueReusableCell(withIdentifier: AboutVerifierCell.className, for: indexPath) as! AboutVerifierCell
            verifier.lblVerifierTitle.text = arrVerifiers[indexPath.row - 2].title
            return verifier
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        if tableView.cellForRow(at: indexPath) is AboutVerifierCell {
            let cellType = arrVerifiers[indexPath.row - 2]
            if cellType == .appDescription {
                router?.routeToAppDescription()
            }else {
                router?.routeToTermOfUse(cellType)
            }
        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        showNavbarSeperator = scrollView.contentOffset.y > 0
    }
}
