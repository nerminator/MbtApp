//
//  MBTTransportationOptionsCompanyLocationList.swift
//
//  Created by Serkut Yegin on 9.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTTransportationOptionsCompanyLocationList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let name = "name"
    static let isDefault = "isDefault"
  }

  // MARK: Properties
  public var id: Int?
  public var name: String?
  public var isDefault: Bool? = false

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    id = try? object.value(for: SerializationKeys.id)
    name = try? object.value(for: SerializationKeys.name)
    isDefault = try? object.value(for: SerializationKeys.isDefault)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = name { dictionary[SerializationKeys.name] = value }
    dictionary[SerializationKeys.isDefault] = isDefault
    return dictionary
  }

}
