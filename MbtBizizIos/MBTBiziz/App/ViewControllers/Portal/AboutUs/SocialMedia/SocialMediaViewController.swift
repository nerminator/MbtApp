//
//  ClubLocsViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 2.12.2023.
//  Copyright © 2023 Serkut Yegin. All rights reserved.
//

import UIKit

class SocialMediaViewController: MBTBaseViewController
{
    public var loc_id = 0;
    
    var dataSource : [MBTMedia] = [];
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        tableView.dataSource = self
        tableView.backgroundView = nil

        WSProvider.shared.wsRequest(.getMedias, map: {(_ response : MBTSocialMediaResponse?) in
            if let medias = response?.medias {
                self.dataSource = medias
                self.tableView.reloadData()
            }
        })
    }
 
}

extension SocialMediaViewController : UITableViewDelegate, UITableViewDataSource {
    
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
        let cell = tableView.dequeueReusableCell(withIdentifier: SocialMediaTableCell.className, for: indexPath) as! SocialMediaTableCell
        
        cell.lblAccount.text = dataSource[indexPath.section].details![indexPath.row].account
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if let details = dataSource[indexPath.section].details, let url = details[indexPath.row].url{
            UIApplication.shared.open(URL(string: url)!)
        }
        
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


