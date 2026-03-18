//
//  MBTWorkCalendarResponse.swift
//
//  Created by Serkut Yegin on 27.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTWorkCalendarResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let year = "year"
    static let dayList = "dayList"
    static let month = "month"
    static let initialDayList = "initialDayList"
  }

  // MARK: Properties
  public var year: Int?
  public var dayList: [MBTWorkCalendarDayList]?
  public var month: Int?
  public var initialDayList: [MBTWorkCalendarInitialDayList]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    year = try? object.value(for: SerializationKeys.year)
    dayList = try? object.value(for: SerializationKeys.dayList)
    month = try? object.value(for: SerializationKeys.month)
    initialDayList = try? object.value(for: SerializationKeys.initialDayList)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = year { dictionary[SerializationKeys.year] = value }
    if let value = dayList { dictionary[SerializationKeys.dayList] = value.map { $0.dictionaryRepresentation() } }
    if let value = month { dictionary[SerializationKeys.month] = value }
    if let value = initialDayList { dictionary[SerializationKeys.initialDayList] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
