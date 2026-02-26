//
//  MBTFoodMenuShuttleInfo.swift
//
//  Created by Serkut Yegin on 12.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTFoodMenuShuttleInfo: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let header = "header"
    static let shuttleList = "shuttleList"
  }

  // MARK: Properties
  public var header: String?
  public var shuttleList: [MBTFoodMenuShuttleList]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    header = try? object.value(for: SerializationKeys.header)
    shuttleList = try? object.value(for: SerializationKeys.shuttleList)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = header { dictionary[SerializationKeys.header] = value }
    if let value = shuttleList { dictionary[SerializationKeys.shuttleList] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
