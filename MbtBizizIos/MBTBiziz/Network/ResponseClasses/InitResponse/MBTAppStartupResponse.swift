//
//  MBTAppStartupResponse.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 25.01.2020.
//  Copyright © 2020 Serkut Yegin. All rights reserved.
//

import UIKit

class MBTAppStartupResponse: DecodableResponse {

    var aboutText: String?
    var appDescriptionText: String?

    enum CodingKeys: String, CodingKey {
        case aboutText
        case appDescriptionText
    }

    required public init(from decoder: Decoder) throws {

        try super.init(from: decoder)
        
        let container = try decoder.container(keyedBy: CodingKeys.self)
        aboutText = try? container.decode(String.self, forKey: .aboutText)
        appDescriptionText = try? container.decode(String.self, forKey: .appDescriptionText)
    }
}
