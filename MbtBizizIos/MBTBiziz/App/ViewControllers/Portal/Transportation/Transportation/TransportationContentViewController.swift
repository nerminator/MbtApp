//
//  TransportationContentViewController.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 14.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

extension TransportationContentViewController {
    
    func setup(with shuttleList: [MBTTransportationListShuttleList], isArrival : Bool?, searchedItem : MBTTransportationListStopList?) {
        self.arrItems = shuttleList
        self.isArrival = isArrival ?? false
        self.searchedItem = searchedItem
        selectedIndexPath = shuttleList.count == 1 ? IndexPath(row: 1, section: 0) : nil
        tableView.reloadData()
    }
    
    func setCurrentCompanyLocation(_ currentCompanyLocation : String?) {
        self.companyLocationItem = currentCompanyLocation
    }
}

class TransportationContentViewController: MBTBaseViewController {

    @IBOutlet weak var tableView: UITableView!
    fileprivate var selectedIndexPath : IndexPath?
    fileprivate var arrItems : [MBTTransportationListShuttleList] = []
    fileprivate var isArrival : Bool = true
    fileprivate var companyLocationItem : String?
    fileprivate var searchedItem : MBTTransportationListStopList?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

}

extension TransportationContentViewController : UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrItems.count + 1
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 { return 56 }
        else if isSelectedIndex(indexPath) { return UITableViewAutomaticDimension }
        else { return 88 }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            return tableView.dequeueReusableCell(withIdentifier: TransportationHeaderCell.className, for: indexPath)
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: TransportationTableCell.className, for: indexPath) as! TransportationTableCell
            cell.setup(indexPath.row - 1, isExpanded: isSelectedIndex(indexPath), searchedItem: searchedItem , shuttleListItem: arrItems[indexPath.row - 1], isArrival: isArrival, companyName: companyLocationItem)
            cell.delegate = self
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.row != 0 {
            if let previous = selectedIndexPath {
                if isSelectedIndex(indexPath) {
                    (tableView.cellForRow(at: previous) as? TransportationTableCell)?.setExpanded(false, animated: true)
                    selectedIndexPath = nil
                } else {
                    (tableView.cellForRow(at: previous) as? TransportationTableCell)?.setExpanded(false, animated: true)
                    (tableView.cellForRow(at: indexPath) as? TransportationTableCell)?.setExpanded(true, animated: true)
                    selectedIndexPath = indexPath
                }
            } else {
                (tableView.cellForRow(at: indexPath) as? TransportationTableCell)?.setExpanded(true, animated: true)
                selectedIndexPath = indexPath
            }
            tableView.beginUpdates()
            tableView.endUpdates()
        }
    }
}

extension TransportationContentViewController : TransportationTableCellDelegate {
    
    func transportationCell(_ transportationCell: TransportationTableCell, didTappedInfoAt index: Int) {
        let item = arrItems[index]
        let controller = DriverInfoViewController.fromStoryboard(.driverInfo)
        controller.driverInfo = item.driverInfo
        controller.locationInfo = item.name
        self.navigationController?.presentAsBottomPopup(to: controller)
    }
    
}

fileprivate extension TransportationContentViewController {
    
    func isSelectedIndex(_ indexPath: IndexPath) -> Bool {
        guard let selected = selectedIndexPath else { return false }
        return selected.row == indexPath.row && selected.section == indexPath.section
    }
}
