//
//  ClubLocsViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 2.12.2023.
//  Copyright © 2023 Serkut Yegin. All rights reserved.
//

import UIKit

class ClubsViewController: MBTBaseViewController, LinksDisplayLogic
{
    public var loc_id = 0;
    
    var dataSource : [MBTClub] = [];
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        tableView.dataSource = self
        WSProvider.shared.wsRequest(.getClubs(loc_id: self.loc_id), map: {(_ response : MBTClubsResponse?) in
            if let clubs = response?.clubs {
                self.dataSource = clubs
                self.tableView.reloadData()
            }
        })
    }
 
}

extension ClubsViewController : UITableViewDelegate, UITableViewDataSource {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return dataSource.count
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return dataSource[section].name
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataSource[section].details?.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: ClubsTableCell.className, for: indexPath) as! ClubsTableCell
        
        cell.lblResponsible.text = dataSource[indexPath.section].details![indexPath.row].responsible
        cell.lblContact.text = dataSource[indexPath.section].details![indexPath.row].contact
        return cell
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        return false
    }
    
    func tableView(_ tableView: UITableView, willDisplayHeaderView view: UIView, forSection section: Int) {
        guard let header = view as? UITableViewHeaderFooterView else { return }
        header.textLabel?.textColor = UIColor.mbtBlue
        header.textLabel?.font = UIFont.boldSystemFont(ofSize: 14)
        header.textLabel?.text = header.textLabel?.text?.uppercased()
        header.textLabel?.frame.w = header.w
    }
    
}


