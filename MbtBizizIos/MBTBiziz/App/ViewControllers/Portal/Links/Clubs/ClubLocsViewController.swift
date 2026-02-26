//
//  ClubLocsViewController.swift
//  MBTBiziz
//
//  Created by Nermy on 2.12.2023.
//  Copyright © 2023 Serkut Yegin. All rights reserved.
//

import Foundation
class ClubLocsViewController: MBTBaseViewController, LinksDisplayLogic, MBTButtonCardViewDelegate
{
    @IBOutlet weak var scrollView: UIScrollView!
    var arrViews : [MBTButtonCardView] = []

    override func viewDidLoad() {
        WSProvider.shared.wsRequest(.getClubLocs, map: {(_ response : MBTClubLocsResponse?) in
            self.arrViews =  self.createSubViews(response: response)
            for view in self.arrViews {
                self.scrollView.addSubview(view)
            }
        })
    }
    private func createSubViews(response: MBTClubLocsResponse?) ->  [MBTButtonCardView] {
        var x=CGFloat(0),  y=CGFloat(0),  w=self.scrollView.frame.width
        var views : [MBTButtonCardView] = []
        if let res = response, let locs = res.locs {
            var h = (self.scrollView.frame.height - 100) / CGFloat(locs.count);
            if h > 220 {h = 220}
            if h < 100 {h = 100}

            for loc in locs{
                var frame = CGRectZero
                frame.x = x; frame.y = y; frame.w = w; frame.h = h - 20;
                let view =  MBTButtonCardView(frame: frame)
                view.title = loc.location
                view.data = loc.id!
                view.delegate = self
                view.cornerRadius = 4
                views.append(view)
                y += h
            }
        }
        return views
    }
    func buttonCardViewDidSelected(_ homeCard: MBTButtonCardView) {
        self.navigationController?.pushViewController(PageContainerViewController.buildPagerForPortalNews(NewsType.clubs, locId: homeCard.data), animated: true)
    }
}


