import Foundation
import Marshal

public final class MBTActivateBusinessCardResponse: MarshalResponse {


    // MARK: Declaration for string constants to be used to decode and also serialize.
    private struct SerializationKeys {
      static let digitalCardUrl = "digitalCardUrl"
    }

    // MARK: Properties
    public var digitalCardUrl: String?

    // MARK: Marshal Initializers

    /// Map a JSON object to this class using Marshal.
    ///
    /// - parameter object: A mapping from ObjectMapper
    public required init(object: MarshaledObject) {
      try! super.init(object: object)
      digitalCardUrl = try? object.value(for: SerializationKeys.digitalCardUrl)
    }

    /// Generates description of the object in the form of a NSDictionary.
    ///
    /// - returns: A Key value pair containing all valid values in the object.
    public func dictionaryRepresentation() -> [String: Any] {
      var dictionary: [String: Any] = [:]
      if let value = digitalCardUrl { dictionary[SerializationKeys.digitalCardUrl] = value }
      return dictionary
    }

  }
