//
//  MBTWorkCalendarDayList.swift
//
//  Created by Serkut Yegin on 27.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTWorkCalendarDayList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let typeList = "typeList"
    static let day = "day"
    static let dayText = "dayText"
  }

  // MARK: Properties
  public var typeList: [MBTWorkCalendarTypeList]?
  public var day: String?
  public var dayText : String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    day = try? object.value(for: SerializationKeys.day)
    dayText = try? object.value(for: SerializationKeys.dayText)
    typeList = try? object.value(for: SerializationKeys.typeList)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = day { dictionary[SerializationKeys.day] = value }
    if let value = dayText { dictionary[SerializationKeys.dayText] = value }
    if let value = typeList { dictionary[SerializationKeys.typeList] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
