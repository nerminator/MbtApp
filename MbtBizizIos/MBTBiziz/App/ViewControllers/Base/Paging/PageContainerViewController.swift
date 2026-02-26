//
//  PageContainerViewController.swift
//
//  Created by Serkut Yegin on 31.01.2018.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit

struct PageItem {
    
    var title : String
    var storyboardIdentifier : Storyboard.Identifier
    var additionalData : [String:Any?]?
    
    init(title: String, storyboardIdentifier: Storyboard.Identifier) {
        self.title = title
        self.storyboardIdentifier = storyboardIdentifier
    }
    
    init(title: String, storyboardIdentifier: Storyboard.Identifier, additionalData: [String:Any?]) {
        self.title = title
        self.storyboardIdentifier = storyboardIdentifier
        self.additionalData = additionalData
    }
}

protocol PageContainerDisplayLogic: class
{
    func displayReloadView(viewModel : PageContainer.Initial.ViewModel)
}

class PageContainerViewController: MBTBaseViewController, PageContainerDisplayLogic
{
    var interactor: PageContainerBusinessLogic?
    var router: (NSObjectProtocol & PageContainerRoutingLogic & PageContainerDataPassing)?
    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var constTabHeight: NSLayoutConstraint!
    @IBOutlet weak var scrollableTabView: ScrollableTabView!
    
    fileprivate var pageViewController : UIPageViewController?
    fileprivate var lastPendingViewControllerIndex : Int = 0
    
    var headers = true
    
    // MARK: Object lifecycle
  
    override init(nibName nibNameOrNil: String?, bundle nibBundleOrNil: Bundle?) {
        super.init(nibName: nibNameOrNil, bundle: nibBundleOrNil)
        PageContainerWorker().doSetup(self)
    
    }
  
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        PageContainerWorker().doSetup(self)
    }
  
    // MARK: View lifecycle
  
    override func viewDidLoad() {
        super.viewDidLoad()
        pageViewController = childViewControllers[0] as? UIPageViewController
        pageViewController?.dataSource = self
        pageViewController?.delegate = self
        interactor?.buildPageItems()
        navigationItem.title = router?.dataStore?.title
        
        //implemented to manage delete action in content tableview
        if let pageScroll = pageViewController?.view.subviews.first as? UIScrollView {
            pageScroll.canCancelContentTouches = false
        }
        if (!headers){
            constTabHeight.constant = 0
        }
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        scrollableTabView.layoutIndicator()
    }
    
    override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        coordinator.animate(alongsideTransition: { (context) in
            self.scrollableTabView.layoutIndicator()
        }, completion: nil)
    }
    
}

extension PageContainerViewController : UIPageViewControllerDataSource, UIPageViewControllerDelegate {
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        guard let viewControllers = interactor?.viewControllers, let viewControllerIndex = interactor?.viewControllers.index(of: viewController) else {
            return nil
        }
        let nextIndex = viewControllerIndex + 1
        guard viewControllers.count > nextIndex else { return nil }
        return viewControllers[nextIndex]
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        guard let viewControllers = interactor?.viewControllers, let viewControllerIndex = interactor?.viewControllers.index(of: viewController) else {
            return nil
        }
        let previousIndex = viewControllerIndex - 1
        guard previousIndex >= 0, viewControllers.count > previousIndex else { return nil }
        return viewControllers[previousIndex]
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, willTransitionTo pendingViewControllers: [UIViewController]) {
        guard pendingViewControllers.count > 0, let viewControllerIndex = interactor?.viewControllers.index(of: pendingViewControllers[0]) else { return }
        self.lastPendingViewControllerIndex = viewControllerIndex
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, didFinishAnimating finished: Bool, previousViewControllers: [UIViewController], transitionCompleted completed: Bool) {
        if completed && lastPendingViewControllerIndex != scrollableTabView.currentPage {
            scrollableTabView.currentPage = lastPendingViewControllerIndex
        }
    }
}

extension PageContainerViewController {
    
    //MARK: Display Logic
    
    func displayReloadView(viewModel : PageContainer.Initial.ViewModel) {
        if isViewLoaded {
            scrollableTabView.setup(interactor?.buttonTitles ?? [], distrubition: viewModel.distrubition, startPage: 0, headers:self
                .headers)
            setupPageView(viewModel.startPage)
        }
    }
    
    fileprivate func setupPageView(_ startPage : Int) {
        guard let viewControllers = interactor?.viewControllers, viewControllers.count > startPage else {
            //pageViewController?.setViewControllers(nil, direction: .forward, animated: false, completion: nil)
            return
        }
        let firstViewController = viewControllers.count > startPage ? [viewControllers[startPage]] : nil
        pageViewController?.setViewControllers(firstViewController, direction: .forward, animated: false, completion: nil)
        scrollableTabView.currentPage = startPage
    }
}

extension PageContainerViewController : ScrollableTabViewDelegate {
    
    func scrollableTabView(_ scrollableTabView: ScrollableTabView, didSelectItemAt index: Int, isForward: Bool) {
        guard let viewControllers = interactor?.viewControllers, viewControllers.count > index else { return }
        DispatchQueue.main.async { [weak self] in
            self?.pageViewController?.setViewControllers([viewControllers[index]], direction: isForward ? .forward : .reverse, animated: true, completion: nil)
        }
    }
}
