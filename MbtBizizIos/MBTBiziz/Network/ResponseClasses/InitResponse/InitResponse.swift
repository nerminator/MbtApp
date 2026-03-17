//
//  InitResponse.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 5.01.2019.
//  Copyright © 2019 Serkut Yegin. All rights reserved.
//

import UIKit

var sharedPhoneList : [PhoneItem]? = nil
var sharedCallPhone: String? = nil

class InitResponse: DecodableResponse {

    var title: String?
    var message: String?
    var buttonList: [ButtonItem]?
    
    enum CodingKeys: String, CodingKey {
        case title
        case message
        case buttonList
        case phoneList
        case callPhone
    }
    
    required public init(from decoder: Decoder) throws {
        
        try super.init(from: decoder)
        let container = try decoder.container(keyedBy: CodingKeys.self)
        title = try? container.decode(String.self, forKey: .title)
        message = try? container.decode(String.self, forKey: .message)
        buttonList = try? container.decode([ButtonItem].self, forKey: .buttonList)
        
        sharedPhoneList = try? container.decode([PhoneItem].self, forKey: .phoneList)
        sharedCallPhone = try? container.decode(String.self, forKey: .callPhone)
    }
}

enum ButtonType: Int, Decodable {
    
    case `continue` = 1
    case download = 2
}

struct ButtonItem: Decodable {
    
    var type: ButtonType?
    var text: String?
    var url: String?
}

struct PhoneItem: Decodable {
    var label: String?
    var phone: String?
}
