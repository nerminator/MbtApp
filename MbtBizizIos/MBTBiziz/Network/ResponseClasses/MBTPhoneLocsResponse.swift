//
//  MBTPhoneLocsResponse.swift
//
//  Created by Serkut Yegin on 8.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTPhoneLocsResponse: MarshalResponse {
    // MARK: Declaration for string constants to be used to decode and also serialize.
    private struct SerializationKeys {
      static let locs = "locs"
    }

    // MARK: Properties
    public var locs: [MBTPhoneLoc]?
    
    // MARK: Marshal Initializers

    /// Map a JSON object to this class using Marshal.
    ///
    /// - parameter object: A mapping from ObjectMapper
    public required init(object: MarshaledObject) {
      try! super.init(object: object)
      locs = try? object.value(for: SerializationKeys.locs)
    }
    
    /// Generates description of the object in the form of a NSDictionary.
    ///
    /// - returns: A Key value pair containing all valid values in the object.
    public func dictionaryRepresentation() -> [String: Any] {
        var dictionary: [String: Any] = [:]
        if let value = locs { dictionary[SerializationKeys.locs] = value.map { $0.dictionaryRepresentation() } }
        return dictionary
    }
}

public class MBTPhoneLoc: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let location = "location"
  }

  // MARK: Properties
  public var id: Int?
  public var location: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    id = try? object.value(for: SerializationKeys.id)
    location = try? object.value(for: SerializationKeys.location)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = location { dictionary[SerializationKeys.location] = value }
    return dictionary
  }

}
