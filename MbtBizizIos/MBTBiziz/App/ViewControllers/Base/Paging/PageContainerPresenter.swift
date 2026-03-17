//
//  PageContainerPresenter.swift
//
//  Created by Serkut Yegin on 31.01.2018.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

protocol PageContainerPresentationLogic
{
    func presentReloadView(response : PageContainer.Initial.Response)
}

class PageContainerPresenter: PageContainerPresentationLogic
{
    weak var viewController: PageContainerDisplayLogic?
    
    func presentReloadView(response : PageContainer.Initial.Response) {
        viewController?.displayReloadView(viewModel: PageContainer.Initial.ViewModel(startPage: response.startPage, distrubition : response.distrubition))
    }
}
