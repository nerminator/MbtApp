//
//  TransportationViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 13.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationDisplayLogic: class
{
    func displayInitializeViewResult(viewModel : Transportation.Initialize.ViewModel)
    func displayValidateInputs(viewModel : Transportation.Validate.ViewModel)
    func displayReloadPageResult(viewModel : Transportation.Reload.ViewModel)
    func displaySendDataResult(viewModel : Transportation.SendData.ViewModel)
    func displaySwitchLocationResult(viewModel : Transportation.SwitchLocations.ViewModel)
    func displayBecomeFirstResponder(viewModel : Transportation.BecomeFirstResponder.ViewModel)
    func displayMakeSearchResult(viewModel : Transportation.SearchResult.ViewModel)
    func displayClearTextFieldResult(viewModel : Transportation.ClearTextField.ViewModel)
}

class TransportationViewController: MBTBaseViewController, TransportationDisplayLogic
{
    // MARK: IBOutlets
    @IBOutlet weak var textFieldDeparture: UITextField!
    @IBOutlet weak var textFieldArrival: UITextField!
    @IBOutlet weak var btnChangeLocations: UIButton!
    @IBOutlet weak var scrollableTabView: ScrollableTabView!
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var constTabHeight: NSLayoutConstraint!
    fileprivate var contentVC : TransportationContentViewController?
    
    // MARK: VIP Protocols
    
    var interactor: TransportationBusinessLogic?
    var router: (NSObjectProtocol & TransportationRoutingLogic & TransportationDataPassing)?

    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        TransportationWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        TransportationWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        textFieldDeparture.attributedPlaceholder = NSAttributedString.init(string: "TXT_SHUTTLE_SHUTTLE_DEST_PLCH".localized(),
                                                                      attributes: [.foregroundColor : UIColor.white.withAlphaComponent(0.5),
                                                                                   .font: UIFont.mbtRegular(18)!])
        textFieldArrival.attributedPlaceholder = NSAttributedString.init(string: "TXT_SHUTTLE_SHUTTLE_ARRIVAL_PLCH".localized(),
                                                                                 attributes: [.foregroundColor : UIColor.white.withAlphaComponent(0.5),
                                                                                              .font: UIFont.mbtRegular(18)!])
        childViewControllers.forEach { (controller) in
            if controller is TransportationContentViewController {
                self.contentVC = controller as? TransportationContentViewController
            }
        }
        interactor?.initializeView(request: Transportation.Initialize.Request())
    }
}

extension TransportationViewController {
    
    @IBAction func btnChangeLocationsTapped(_ sender: UIButton) {
        let clearButtonMode = textFieldDeparture.clearButtonMode
        let departureText = textFieldDeparture.text
        textFieldDeparture.text = textFieldArrival.text
        textFieldArrival.text = departureText
        textFieldDeparture.clearButtonMode = textFieldArrival.clearButtonMode
        textFieldArrival.clearButtonMode = clearButtonMode
        interactor?.switchLocations(request: Transportation.SwitchLocations.Request())
    }
}

extension TransportationViewController {
    
    //MARK: - Display Logic
    func displayInitializeViewResult(viewModel : Transportation.Initialize.ViewModel) {
        constTabHeight.constant = viewModel.tabItems.count == 1 ? 0 : 53
        scrollableTabView.setup(viewModel.tabItems, distrubition: .equal(spacing: 0))
        contentVC?.setCurrentCompanyLocation(viewModel.arrivalText)
        contentVC?.setup(with: viewModel.shuttleList, isArrival: interactor?.isArrival, searchedItem: interactor?.searchedStopItem)
        textFieldArrival.text = viewModel.arrivalText
        if scrollableTabView.h != constTabHeight.constant {
            view.layoutIfNeeded()
        }
        btnChangeLocationsTapped(self.btnChangeLocations); // reverse initial order
    }
    
    func displayValidateInputs(viewModel : Transportation.Validate.ViewModel) { }
    
    func displayReloadPageResult(viewModel : Transportation.Reload.ViewModel) {
        contentVC?.setup(with: viewModel.shuttleList, isArrival: interactor?.isArrival, searchedItem: interactor?.searchedStopItem)
    }
    
    func displaySendDataResult(viewModel : Transportation.SendData.ViewModel) { }
    
    func displaySwitchLocationResult(viewModel: Transportation.SwitchLocations.ViewModel) {
        constTabHeight.constant = viewModel.tabItems.count == 1 ? 0 : 53
        scrollableTabView.setup(viewModel.tabItems, distrubition: .equal(spacing: 0))
        contentVC?.setup(with: viewModel.shuttleList, isArrival: interactor?.isArrival, searchedItem: interactor?.searchedStopItem)
        if scrollableTabView.h != constTabHeight.constant {
            view.layoutIfNeeded()
        }
    }
    
    func displayBecomeFirstResponder(viewModel: Transportation.BecomeFirstResponder.ViewModel) {
        if viewModel.needToRouteSearch {
            router?.routeToSearch(viewModel.shuttleList, searchCompletion: { [weak self] (searchStopItem) in
                self?.interactor?.makeSearch(request: Transportation.SearchResult.Request(selectedSearchItem: searchStopItem))
            })
        }
    }
    
    func displayMakeSearchResult(viewModel: Transportation.SearchResult.ViewModel) {
        textFieldArrival.text = viewModel.arrivalText
        textFieldDeparture.text = viewModel.departureText
        contentVC?.setup(with: viewModel.shuttleList, isArrival: interactor?.isArrival, searchedItem: interactor?.searchedStopItem)
    }
    
    func displayClearTextFieldResult(viewModel: Transportation.ClearTextField.ViewModel) {
        textFieldArrival.text = viewModel.arrivalText
        textFieldDeparture.text = viewModel.departureText
        contentVC?.setup(with: viewModel.shuttleList, isArrival: interactor?.isArrival, searchedItem: interactor?.searchedStopItem)
    }
}

extension TransportationViewController : UITextFieldDelegate {
    
    func textFieldShouldBeginEditing(_ textField: UITextField) -> Bool {
        interactor?.becomeFirstResponder(request: Transportation.BecomeFirstResponder.Request(isArrivalField: textField == textFieldArrival))
        return false
    }
    
    func textFieldShouldClear(_ textField: UITextField) -> Bool {
        interactor?.clearTextField(request: Transportation.ClearTextField.Request())
        return false
    }
    
}

extension TransportationViewController : ScrollableTabViewDelegate {
    
    func scrollableTabView(_ scrollableTabView: ScrollableTabView, didSelectItemAt index: Int, isForward: Bool) {
        interactor?.reloadPage(request: Transportation.Reload.Request(selectedTabIndex: index))
    }
}
