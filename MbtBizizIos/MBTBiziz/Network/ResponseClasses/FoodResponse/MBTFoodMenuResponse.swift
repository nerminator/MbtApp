//
//  MBTFoodMenuResponse.swift
//
//  Created by Serkut Yegin on 12.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTFoodMenuResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let foodInfo = "foodInfo"
    static let shuttleInfo = "shuttleInfo"
    static let densityInfo = "densityList"
  }

  // MARK: Properties
  public var foodInfo: [MBTFoodMenuFoodInfo]?
  public var shuttleInfo: MBTFoodMenuShuttleInfo?
  public var densityInfo: [MBTFoodMenuDensityInfo]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    foodInfo = try? object.value(for: SerializationKeys.foodInfo)
    shuttleInfo = try? object.value(for: SerializationKeys.shuttleInfo)
    densityInfo = try? object.value(for: SerializationKeys.densityInfo)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = foodInfo { dictionary[SerializationKeys.foodInfo] = value.map { $0.dictionaryRepresentation() } }
    if let value = shuttleInfo { dictionary[SerializationKeys.shuttleInfo] = value.dictionaryRepresentation() }
    if let value = densityInfo { dictionary[SerializationKeys.densityInfo] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
