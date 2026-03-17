//
//  ClubLocsViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 2.12.2023.
//  Copyright © 2023 Serkut Yegin. All rights reserved.
//

import UIKit

extension String {
   func replace(string:String, replacement:String) -> String {
       return self.replacingOccurrences(of: string, with: replacement, options: NSString.CompareOptions.literal, range: nil)
   }

   func removeWhitespace() -> String {
       return self.replace(string: " ", replacement: "")
   }
 }

class PhonesViewController: MBTBaseViewController, LinksDisplayLogic
{
    public var loc_id = 0;
    
    var dataSource : [MBTPhone] = [];
    var santral : String = "";
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        tableView.dataSource = self
        tableView.delegate = self
        self.tableView.allowsSelection = true
        WSProvider.shared.wsRequest(.getPhones(loc_id: self.loc_id), map: {(_ response : MBTPhonesResponse?) in
            if let phones = response?.phones {
                self.dataSource = phones
                self.tableView.reloadData()
            }
            if let santral = response?.santral {
                self.santral = santral
            }
        })
    }
 
}

extension PhonesViewController : UITableViewDelegate, UITableViewDataSource {
    
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
        let cell = tableView.dequeueReusableCell(withIdentifier: PhonesTableCell.className, for: indexPath) as! PhonesTableCell
        cell.selectionStyle = .none

        cell.lblUnit.text = dataSource[indexPath.section].details![indexPath.row].unit
        cell.lblNotes.text = dataSource[indexPath.section].details![indexPath.row].notes
        cell.btnInternal.setTitle(dataSource[indexPath.section].details![indexPath.row].intercom, for: .normal)
        
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
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if let internalPhone = dataSource[indexPath.section].details![indexPath.row].intercom{
            
            var trimmedString = internalPhone.replacingOccurrences(of: #"\s?\([\w\s]*\)"#, with: "", options: .regularExpression)
            
            trimmedString = trimmedString.removeWhitespace()
            
            if (trimmedString.length == 4) {

                if let url = URL(string: "tel://" + self.santral + trimmedString) {
                    UIApplication.shared.open(url)
                }

            } else if (trimmedString.length == 11) {

                if let url = URL(string: "tel://" + trimmedString){
                    UIApplication.shared.open(url)
                }
            }
        }
    }
}


