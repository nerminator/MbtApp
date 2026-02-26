//
//  MBTBasicResponse.swift
//  MBTBiziz
//
//  Created by Nermy on 12.08.2025.
//  Copyright © 2025 Onion Tech. All rights reserved.
//


import Foundation
import Marshal

public class MBTBasicResponse: MarshalResponse {

    public required init(object: MarshaledObject) {
        try! super.init(object: object)

    }
}
