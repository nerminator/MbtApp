//
//  TransportationSearchViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationSearchDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : TransportationSearch.Initialize.ViewModel)
    func displayValidateInputs(viewModel : TransportationSearch.Validate.ViewModel)
    func displayReloadPageResult(viewModel : TransportationSearch.Reload.ViewModel)
    func displaySendDataResult(viewModel : TransportationSearch.SendData.ViewModel)
}

class TransportationSearchViewController: MBTBaseViewController, TransportationSearchDisplayLogic
{
    
    // MARK: IBActions
    
    @IBOutlet weak var tableViewPreviousSearch: UITableView!
    @IBOutlet weak var viewPreviousSearch: UIView!
    @IBOutlet weak var tableViewSearchResult: UITableView!
    @IBOutlet weak var viewSearchResult: UIView!
    fileprivate var searchBar = UISearchBar()
    @IBOutlet weak var constTableSearchResultBottom: NSLayoutConstraint!
    @IBOutlet weak var constTablePreviousSearchBottom: NSLayoutConstraint!
    
    // MARK: VIP Protocols
    
    var interactor: TransportationSearchBusinessLogic?
    var router: (NSObjectProtocol & TransportationSearchRoutingLogic & TransportationSearchDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        TransportationSearchWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        TransportationSearchWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationItem.titleView = searchBar
        searchBar.searchBarStyle = .minimal
        searchBar.barTintColor = UIColor.clear
        searchBar.tintColor = UIColor.mbtBlue
        searchBar.backgroundColor = UIColor.clear
        searchBar.keyboardAppearance = .dark
        searchBar.delegate = self
        interactor?.initializeView(request: TransportationSearch.Initialize.Request())
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        searchBar.becomeFirstResponder()
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        searchBar.resignFirstResponder()
    }
    
    override func shouldListenKeyboardNotification() -> Bool {
        return true
    }
    
    override func keyboardAnimation(willShow: Bool, endFrame: CGRect) {
        constTableSearchResultBottom.constant = willShow ? endFrame.h - UIView.safeAreaBottomInset : 0
        constTablePreviousSearchBottom.constant = willShow ? endFrame.h - UIView.safeAreaBottomInset : 0
    }
}

extension TransportationSearchViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : TransportationSearch.Initialize.ViewModel) {
        tableViewSearchResult.reloadData()
    }
    
    func displayValidateInputs(viewModel : TransportationSearch.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : TransportationSearch.Reload.ViewModel) {
        tableViewSearchResult.reloadData()
    }
    
    func displaySendDataResult(viewModel : TransportationSearch.SendData.ViewModel) { }
}

extension TransportationSearchViewController : UISearchBarDelegate {
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        viewSearchResult.isHidden = searchText.isEmpty
        viewPreviousSearch.isHidden = !searchText.isEmpty
        interactor?.reloadPage(request: TransportationSearch.Reload.Request(searchText: searchText))
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
    }
}

extension TransportationSearchViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if tableView == tableViewSearchResult {
            return interactor?.filteredStopList.count ?? 0
        } else {
            return interactor?.previousSearchItems.count ?? 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if tableView == tableViewSearchResult {
            let cell = tableView.dequeueReusableCell(withIdentifier: TransportationSearchResultCell.className, for: indexPath) as! TransportationSearchResultCell
            cell.setup(interactor?.filteredStopList[indexPath.row], index: indexPath.row)
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: TransportationPreviousSearchCell.className, for: indexPath) as! TransportationPreviousSearchCell
            cell.setup(interactor?.previousSearchItems[indexPath.row])
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if tableView == tableViewSearchResult {
            interactor?.validateInputs(request: TransportationSearch.Validate.Request(index: indexPath.row))
            router?.routeToBack(interactor?.filteredStopList[indexPath.row])
        } else {
            if let searchItem = interactor?.previousSearchItems[indexPath.row] {
                router?.routeToBack(MBTTransportationListStopList(from: searchItem))
            }
        }
    }
    
}
