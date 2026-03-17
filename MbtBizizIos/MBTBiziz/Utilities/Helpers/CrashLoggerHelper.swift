import Foundation
import UIKit

class CrashLoggerHelper {

    static let shared = CrashLoggerHelper()

    private init() {}

    func logCrash(message: String, stackTrace: String? = nil) {
        guard let url = URL(string: "https://your.backend.api/crash-log") else { return }

        var request = URLRequest(url: url)
        request.httpMethod = "POST"

        let payload: [String: Any] = [
            "platform": "iOS",
            "message": message,
            "stack_trace": stackTrace ?? "",
            "device_model": UIDevice.current.model,
            "os_version": UIDevice.current.systemVersion,
            "app_version": Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "unknown"
        ]

        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        request.httpBody = try? JSONSerialization.data(withJSONObject: payload, options: [])

        let task = URLSession.shared.dataTask(with: request) { _, response, error in
            if let error = error {
                print("Crash log failed: \(error)")
            } else if let httpResponse = response as? HTTPURLResponse {
                print("Crash log status: \(httpResponse.statusCode)")
            }
        }

        task.resume()
    }
}
