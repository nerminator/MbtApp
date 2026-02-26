//
//  MBTBaseHomeViewController.swift
//  MBT
//
//  Created by Serkut Yegin on 18.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

class MBTBaseHomeViewController: MBTBaseViewController {

    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func scrollToTop() {
        if self.tableView.numberOfSections > 0 {
            tableView.scrollToRow(at: IndexPath(row: Foundation.NSNotFound, section: 0), at: .top, animated: true)
        }
    }
}
