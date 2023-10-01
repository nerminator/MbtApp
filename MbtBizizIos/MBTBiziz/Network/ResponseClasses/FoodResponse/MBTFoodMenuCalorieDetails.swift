//
//  MBTFoodMenuCalorieDetails.swift
//
//  Created by Serkut Yegin on 12.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTFoodMenuCalorieDetails: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let name = "name"
    static let value = "value"
  }

  // MARK: Properties
  public var name: String?
  public var value: Int?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    name = try? object.value(for: SerializationKeys.name)
    value = try? object.value(for: SerializationKeys.value)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = name { dictionary[SerializationKeys.name] = value }
    if let value = value { dictionary[SerializationKeys.value] = value }
    return dictionary
  }

}
