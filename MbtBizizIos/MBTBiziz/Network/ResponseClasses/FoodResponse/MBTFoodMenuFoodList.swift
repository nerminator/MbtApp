//
//  MBTFoodMenuFoodList.swift
//
//  Created by Serkut Yegin on 12.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTFoodMenuFoodList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let info = "info"
    static let name = "name"
    static let calorie = "calorie"
    static let calorieDetails = "detailList"
  }

  // MARK: Properties
  public var info: String?
  public var name: String?
  public var calorie: Int?
  public var calorieDetails: [MBTFoodMenuCalorieDetails]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    info = try? object.value(for: SerializationKeys.info)
    name = try? object.value(for: SerializationKeys.name)
    calorie = try? object.value(for: SerializationKeys.calorie)
    calorieDetails = try? object.value(for: SerializationKeys.calorieDetails)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = info { dictionary[SerializationKeys.info] = value }
    if let value = name { dictionary[SerializationKeys.name] = value }
    if let value = calorie { dictionary[SerializationKeys.calorie] = value }
    if let value = calorieDetails { dictionary[SerializationKeys.calorieDetails] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
