import Foundation
import Marshal

public class MBTPayslipResponse: MarshalResponse {
    private struct Keys {
        static let base64 = "base64"
    }

    public var base64: String?

    public required init(object: MarshaledObject) {
        try! super.init(object: object)
        base64 = try? object.value(for: Keys.base64)
    }
}
