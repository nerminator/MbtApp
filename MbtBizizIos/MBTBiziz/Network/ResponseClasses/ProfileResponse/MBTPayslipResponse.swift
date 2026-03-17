//
//  MBTPayslipResponse.swift
//  MBTBiziz
//
//  Created by Nermy on 14.08.2025.
//

import Foundation
import Marshal

/// Backend shape:
/// {
///   "statusCode": 200,
///   "responseData": { "base64": "JVBERi0xLjQK..." },
///   "errorMessage": null
/// }
public final class MBTPayslipResponse: MarshalResponse {
    public var base64: String?

    public required init(object: MarshaledObject) throws {
        try super.init(object: object)

        // 1) Try the canonical wrapper key used in your app (KEY_PAYLOAD_MESSAGE = "responseData")
        if let payload: [String: Any] = try? object.value(for: KEY_PAYLOAD_MESSAGE) {
            base64 = payload["base64"] as? String
        }

        // 2) Fallbacks in case backend returns a different shape
        if base64 == nil, let payload: [String: Any] = try? object.value(for: "data") {
            base64 = payload["base64"] as? String
        }
        if base64 == nil {
            base64 = try? object.value(for: "base64")
        }
    }
}
