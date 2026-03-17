//
//  MBTTransportationListDriverInfo.swift
//
//  Created by Serkut Yegin on 9.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTTransportationListDriverInfo: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let licensePlate = "licensePlate"
    static let name = "name"
    static let telephone = "telephone"
  }

  // MARK: Properties
  public var licensePlate: String?
  public var name: String?
  public var telephone: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    licensePlate = try? object.value(for: SerializationKeys.licensePlate)
    name = try? object.value(for: SerializationKeys.name)
    telephone = try? object.value(for: SerializationKeys.telephone)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = licensePlate { dictionary[SerializationKeys.licensePlate] = value }
    if let value = name { dictionary[SerializationKeys.name] = value }
    if let value = telephone { dictionary[SerializationKeys.telephone] = value }
    return dictionary
  }

}
