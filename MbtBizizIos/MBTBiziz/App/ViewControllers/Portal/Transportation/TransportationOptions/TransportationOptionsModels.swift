//
//  TransportationOptionsModels.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 9.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit

enum TransportationOptions
{
    // MARK: Use cases
  
    enum Initialize {
        struct Request {
            var textFieldType : UITextField
            var textFieldCompanyList : UITextField
        }
        
        struct Response {
            var textFieldType : UITextField
            var textFieldCompanyList : UITextField
        }
        
        struct ViewModel {
            var pickerModelType : CommonPickerModel
            var pickerModelCompanyList : CommonPickerModel
        }
    }
    
    enum Validate {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum Reload {
        struct Request { }
        
        struct Response { }
        
        struct ViewModel { }
    }
    
    enum SendData {
        struct Request {
            var selectedRowType : Int
            var selectedRowCompanyList : Int
        }
        
        struct Response {
            var companyListItem : MBTTransportationOptionsCompanyLocationList
            var response : MBTTransportationListResponse
        }
        
        struct ViewModel {
            var companyListItem : MBTTransportationOptionsCompanyLocationList
            var response : MBTTransportationListResponse
        }
    }
    
}
