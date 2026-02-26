//
//  TransportationTableCell.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 14.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol TransportationTableCellDelegate : class {
    
    func transportationCell(_ transportationCell : TransportationTableCell, didTappedInfoAt index:Int)
}

class TransportationTableCell: UITableViewCell {
    
    fileprivate var arrItems : [MBTTransportationListStopList] = []

    weak var delegate : TransportationTableCellDelegate?
    @IBOutlet weak var lblLocation: BaseUILabelDemi!
    @IBOutlet weak var lblDepartureTime: BaseUILabelLight!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var constTableHeight: NSLayoutConstraint!
    @IBOutlet weak var imgViewArrow: UIImageView!
    fileprivate var index : Int?
    fileprivate var driverInfo: MBTTransportationListDriverInfo?
    fileprivate var departureTime : String = ""
    fileprivate var arrivalTime : String = ""
    fileprivate var searchedItem : MBTTransportationListStopList?
    
    @IBAction func btnInfoTapped(_ sender: UIButton) {
        guard let index = index else { return }
        delegate?.transportationCell(self, didTappedInfoAt: index)
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        tableView.reloadData()
        constTableHeight.constant = CGFloat(44 * arrItems.count)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        // Configure the view for the selected state
    }
    
    func setup(_ index:Int, isExpanded:Bool, searchedItem:MBTTransportationListStopList?, shuttleListItem: MBTTransportationListShuttleList, isArrival : Bool, companyName : String?) {
        self.searchedItem = searchedItem
        self.index = index
        self.departureTime = shuttleListItem.departureTime ?? ""
        self.arrivalTime = shuttleListItem.arrivalTime ?? ""
        lblLocation.text = shuttleListItem.name
        lblDepartureTime.text = shuttleListItem.departureTime
        self.driverInfo = shuttleListItem.driverInfo
        var listItem = shuttleListItem
        listItem.appendCompanyName(companyName, isArrival: isArrival)
        self.arrItems = listItem.stopList ?? []
        constTableHeight.constant = CGFloat(44 * arrItems.count)
        setExpanded(isExpanded)
    }
    
    func setExpanded(_ expanded:Bool, animated:Bool = false) {
        if expanded {
            tableView.reloadData()
        }
        if animated {
            UIView.animate(withDuration: 0.2) {
                self.imgViewArrow.transform = expanded ? CGAffineTransform(rotationAngle: .pi) : .identity
            }
        } else {
            imgViewArrow.transform = expanded ? CGAffineTransform(rotationAngle: .pi) : .identity
        }
    }

}

extension TransportationTableCell : UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return arrItems.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: TransportationRouteCell.className, for: indexPath) as! TransportationRouteCell
        let item = arrItems[indexPath.row]
        var setupText = item.name ?? ""
        if indexPath.row == 0 {
            setupText = setupText + " - " + departureTime + " (\("TXT_SHUTTLE_SHUTTLE_DESTINATION_SMALL".localized()))"
        } else if indexPath.row == arrItems.count - 1 {
            setupText = setupText + " - " + arrivalTime + " (\("TXT_SHUTTLE_SHUTTLE_ARRIVAL_SMALL".localized()))"
        }
        cell.setup(setupText, isSelected: isSearched(item), isLast: indexPath.row == arrItems.count - 1)
        return cell
    }
    
    fileprivate func isSearched(_ item : MBTTransportationListStopList) -> Bool {
        guard let searchItemId = searchedItem?.name, let itemId = item.name else { return false }
        return searchItemId == itemId
    }
}
