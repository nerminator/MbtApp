//
//  NewsDetailInteractor.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 7.05.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

struct NewsDetailItem {
    var title : String
    var content : String
}

protocol NewsDetailBusinessLogic
{
    func initializeView(request : NewsDetail.Initialize.Request)
    func validateInputs(request : NewsDetail.Validate.Request)
    func reloadPage(request : NewsDetail.Reload.Request)
    func sendData(request : NewsDetail.SendData.Request)
    func getUserDiscountCode(request : NewsDetail.GetUserDiscountCode.Request)
    
    var newsDetail : MBTNewsDetailResponse? { get set }
    var userDiscoutCode : MBTGetDiscountCodeResponse? { get set }
    var newsDetailContent : [NewsDetailItem] { get set }
    
    var hasImage : Bool { get }
}

protocol NewsDetailDataStore
{
    var listItem : NewsProtocol? { get set }
}

class NewsDetailInteractor: NewsDetailBusinessLogic, NewsDetailDataStore
{
    var presenter: NewsDetailPresentationLogic?
    var worker = NewsDetailWorker()
    var listItem: NewsProtocol?
    var newsDetail: MBTNewsDetailResponse?
    var userDiscoutCode: MBTGetDiscountCodeResponse?
    var newsDetailContent: [NewsDetailItem] = []
    
    var hasImage: Bool {
        return newsDetail?.images != nil && newsDetail!.images!.count > 0
    }
    
    func initializeView(request : NewsDetail.Initialize.Request) {
        let title = listItem?.discountTypeEnum?.title ?? listItem?.typeEnum?.title
        presenter?.presentInitializeViewResult(response: NewsDetail.Initialize.Response(title: title))
        
    }
    
    func validateInputs(request : NewsDetail.Validate.Request) {
        presenter?.presentValidateInputs(response: NewsDetail.Validate.Response())
    }
    
    func reloadPage(request : NewsDetail.Reload.Request) {
        presenter?.presentReloadPageResult(response : NewsDetail.Reload.Response())
    }
    
    func sendData(request : NewsDetail.SendData.Request) {
        guard let identifier = listItem?.identifier else { return }
        worker.getNewsDetail(identifier) { [weak self] (response) in
            self?.newsDetail = response
            self?.newsDetailContent = []
            if let title = response?.title, let content = response?.text, !title.isEmpty, !content.isEmpty {
                self?.newsDetailContent.append(NewsDetailItem.init(title: title, content: content))
            }
            if let title = response?.subTitle, let content = response?.subText, !title.isEmpty, !content.isEmpty {
                self?.newsDetailContent.append(NewsDetailItem.init(title: title, content: content))
            }
            
            self?.presenter?.presentSendDataResult(response : NewsDetail.SendData.Response())
        }
    }
    
    func getUserDiscountCode(request : NewsDetail.GetUserDiscountCode.Request) {
        guard let identifier = listItem?.identifier else { return }
        worker.getDiscountCode(identifier) { [weak self] (response) in
            if let res = response {
                self?.presenter?.presentGetUserDiscountResult(response : res)
            } else {
                self?.presenter?.presentNoDiscountResult()
            }
        }
    }
    
}
