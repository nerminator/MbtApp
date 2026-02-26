//
//  MBTTransportationOptionsTypeList.swift
//
//  Created by Serkut Yegin on 9.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTTransportationOptionsTypeList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let type = "type"
    static let name = "name"
  }

  // MARK: Properties
  public var type: Int?
  public var name: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    type = try? object.value(for: SerializationKeys.type)
    name = try? object.value(for: SerializationKeys.name)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = type { dictionary[SerializationKeys.type] = value }
    if let value = name { dictionary[SerializationKeys.name] = value }
    return dictionary
  }

}
