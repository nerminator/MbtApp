//
//  PageContainerModels.swift
//
//  Created by Serkut Yegin on 31.01.2018.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

enum PageContainer
{
    // MARK: Use cases
  
    enum Initial {
        struct Request
        {
        }
        struct Response
        {
            var startPage : Int
            var distrubition : TabDistrubition
        }
        struct ViewModel
        {
            var startPage : Int
            var distrubition : TabDistrubition
        }
    }
}
