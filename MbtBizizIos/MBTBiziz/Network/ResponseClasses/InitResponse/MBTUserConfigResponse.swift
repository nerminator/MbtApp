//
//  MBTUserConfigResponse.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 25.01.2020.
//  Copyright © 2020 Serkut Yegin. All rights reserved.
//

import UIKit

class MBTUserConfigResponse: DecodableResponse {

    var shouldShowQrCode: Bool?

    enum CodingKeys: String, CodingKey {
        case shouldShowQrCode
    }

    required public init(from decoder: Decoder) throws {

        try super.init(from: decoder)

        let container = try decoder.container(keyedBy: CodingKeys.self)
        shouldShowQrCode = try? container.decode(Bool.self, forKey: .shouldShowQrCode)
    }
}
