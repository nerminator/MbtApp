//
//  NewsInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 6.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

protocol NewsBusinessLogic
{
    func initializeView(request : News.Initialize.Request)
    func validateInputs(request : News.Validate.Request)
    func reloadPage(request : News.Reload.Request)
    func sendData(request : News.SendData.Request)
    
    var headers : [String] { get set }
    var items : [[MBTNewsNewsList]] { get set }
    var newsType : NewsType { get set }
    var discountType : DiscountType { get set }
    var currentPage : Int { get set }
}

protocol NewsDataStore
{
    
}

class NewsInteractor: NewsBusinessLogic, NewsDataStore
{
    var presenter: NewsPresentationLogic?
    var worker = NewsWorker()
    var newsType: NewsType = .hepsi
    var discountType: DiscountType = .tumu
    var headers: [String] = []
    var items: [[MBTNewsNewsList]] = []
    var currentPage: Int = 0
    var newsRequest : MBTCancellable?
    
    func initializeView(request : News.Initialize.Request) {
        if let type = request.additionalData[MBTConstants.UserDefined.NewsTypeKey] as? NewsType {
            self.newsType = type
            presenter?.presentInitializeViewResult(response: News.Initialize.Response(newsType: type))
        }
    }
    
    func validateInputs(request : News.Validate.Request) {
        presenter?.presentValidateInputs(response: News.Validate.Response())
    }
    
    func reloadPage(request : News.Reload.Request) {
        newsRequest?.cancel()
        currentPage = 0
        worker.getNewsList(newsType, discountType: newsType == .indirim ? discountType : nil, pageNumber: currentPage) { [weak self] (response) in
            self?.headers = []
            self?.items = []
            self?.appendItemsToList(response?.newsList)
            self?.presenter?.presentReloadPageResult(response : News.Reload.Response(birthdayCount: response?.birthdayCount))
        }
    }
    
    func sendData(request : News.SendData.Request) {
        newsRequest = worker.getNewsList(newsType, discountType: newsType == .indirim ? discountType : nil, pageNumber: currentPage + 1, completion: { [weak self] (response) in
            if let response = response {
                self?.currentPage += 1
                self?.appendItemsToList(response.newsList)
                self?.presenter?.presentSendDataResult(response : News.SendData.Response())
            }
        })
    }
    
    fileprivate func appendItemsToList(_ items : [MBTNewsNewsList]?) {
        items?.forEach({ (newsListItem) in
            if let header = newsListItem.monthName {
                if let index = headers.index(of: header) {
                    self.items[index].append(newsListItem)
                } else {
                    self.headers.append(header)
                    self.items.append([newsListItem])
                }
            }
        })
    }
  
}
