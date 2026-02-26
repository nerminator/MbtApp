//
//  MBTFlexibleMonthlyDayList.swift
//
//  Created by Serkut Yegin on 27.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTFlexibleMonthlyDayList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let totalHours = "totalHours"
    static let plusHours = "plusHours"
    static let hoursText = "hoursText"
    static let dayText = "dayText"
    static let minusHours = "minusHours"
  }

  // MARK: Properties
  public var totalHours: String?
  public var plusHours: String?
  public var hoursText: String?
  public var dayText: String?
  public var minusHours: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    totalHours = try? object.value(for: SerializationKeys.totalHours)
    plusHours = try? object.value(for: SerializationKeys.plusHours)
    hoursText = try? object.value(for: SerializationKeys.hoursText)
    dayText = try? object.value(for: SerializationKeys.dayText)
    minusHours = try? object.value(for: SerializationKeys.minusHours)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = totalHours { dictionary[SerializationKeys.totalHours] = value }
    if let value = plusHours { dictionary[SerializationKeys.plusHours] = value }
    if let value = hoursText { dictionary[SerializationKeys.hoursText] = value }
    if let value = dayText { dictionary[SerializationKeys.dayText] = value }
    if let value = minusHours { dictionary[SerializationKeys.minusHours] = value }
    return dictionary
  }

}
