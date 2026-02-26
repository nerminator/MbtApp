//
//  LocationSearchViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 26.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol LocationSearchDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : LocationSearch.Initialize.ViewModel)
    func displayValidateInputs(viewModel : LocationSearch.Validate.ViewModel)
    func displayReloadPageResult(viewModel : LocationSearch.Reload.ViewModel)
    func displaySendDataResult(viewModel : LocationSearch.SendData.ViewModel)
}

class LocationSearchViewController: MBTBaseViewController, LocationSearchDisplayLogic
{
    
    @IBOutlet weak var constTableBottom: NSLayoutConstraint!
    @IBOutlet weak var tableView: UITableView!
    fileprivate var searchBar = UISearchBar()
    // MARK: VIP Protocols
    
    var interactor: LocationSearchBusinessLogic?
    var router: (NSObjectProtocol & LocationSearchRoutingLogic & LocationSearchDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        LocationSearchWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        LocationSearchWorker().doSetup(self)
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
        interactor?.initializeView(request: LocationSearch.Initialize.Request())
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
        constTableBottom.constant = willShow ? endFrame.h - UIView.safeAreaBottomInset : 0
    }
}

extension LocationSearchViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : LocationSearch.Initialize.ViewModel) { }
    
    func displayValidateInputs(viewModel : LocationSearch.Validate.ViewModel) {
        router?.routeToLocationResult(viewModel.building, room: viewModel.room)
    }
    
    func displayReloadPageResult(viewModel : LocationSearch.Reload.ViewModel) {
        tableView.reloadData()
    }
    
    func displaySendDataResult(viewModel : LocationSearch.SendData.ViewModel) { }
}

extension LocationSearchViewController : UISearchBarDelegate {
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        interactor?.reloadPage(request: LocationSearch.Reload.Request(searchText: searchText))
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchBar.resignFirstResponder()
    }
}

extension LocationSearchViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return interactor?.filteredRoomList.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: LocationSearchCell.className, for: indexPath) as! LocationSearchCell
        cell.setup(interactor?.filteredRoomList[indexPath.row], index: indexPath.row)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        interactor?.validateInputs(request: LocationSearch.Validate.Request(index: indexPath.row))
    }
}
