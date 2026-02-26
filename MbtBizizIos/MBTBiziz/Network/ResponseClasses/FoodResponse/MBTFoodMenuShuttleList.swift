//
//  MBTFoodMenuShuttleList.swift
//
//  Created by Serkut Yegin on 12.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTFoodMenuShuttleList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let location = "location"
    static let timeList = "timeList"
  }

  // MARK: Properties
  public var location: String?
  public var timeList: [MBTFoodMenuTimeList]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    location = try? object.value(for: SerializationKeys.location)
    timeList = try? object.value(for: SerializationKeys.timeList)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = location { dictionary[SerializationKeys.location] = value }
    if let value = timeList { dictionary[SerializationKeys.timeList] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
