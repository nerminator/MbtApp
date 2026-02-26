//
//  MBTFoodMenuFoodInfo.swift
//
//  Created by Serkut Yegin on 12.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTFoodMenuFoodInfo: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let isToday = "isToday"
    static let dateTitle = "dateTitle"
    static let foodList = "foodList"
    static let dateText = "dateText"
  }

  // MARK: Properties
  public var isToday: Bool? = false
  public var dateTitle: String?
  public var foodList: [MBTFoodMenuFoodList]?
  public var dateText: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    isToday = try? object.value(for: SerializationKeys.isToday)
    dateTitle = try? object.value(for: SerializationKeys.dateTitle)
    foodList = try? object.value(for: SerializationKeys.foodList)
    dateText = try? object.value(for: SerializationKeys.dateText)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    dictionary[SerializationKeys.isToday] = isToday
    if let value = dateTitle { dictionary[SerializationKeys.dateTitle] = value }
    if let value = foodList { dictionary[SerializationKeys.foodList] = value.map { $0.dictionaryRepresentation() } }
    if let value = dateText { dictionary[SerializationKeys.dateText] = value }
    return dictionary
  }

}
