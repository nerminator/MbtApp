//
//  MBTFlexibleYearlyResponse.swift
//
//  Created by Serkut Yegin on 27.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTFlexibleYearlyResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let monthList = "monthList"
    static let selectedYear = "selectedYear"
    static let yearList = "yearList"
    static let plusHoursText = "plusHoursText"
    static let minusHoursText = "minusHoursText"
  }

  // MARK: Properties
  public var monthList: [MBTFlexibleYearlyMonthList]?
  public var selectedYear: Int?
  public var yearList: [Int]?
    public var plusHoursText : String?
    public var minusHoursText : String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    monthList = try? object.value(for: SerializationKeys.monthList)
    selectedYear = try? object.value(for: SerializationKeys.selectedYear)
    yearList = try? object.value(for: SerializationKeys.yearList)
    plusHoursText = try? object.value(for: SerializationKeys.plusHoursText)
    minusHoursText = try? object.value(for: SerializationKeys.minusHoursText)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = monthList { dictionary[SerializationKeys.monthList] = value.map { $0.dictionaryRepresentation() } }
    if let value = selectedYear { dictionary[SerializationKeys.selectedYear] = value }
    if let value = yearList { dictionary[SerializationKeys.yearList] = value }
    if let value = plusHoursText { dictionary[SerializationKeys.plusHoursText] = value }
    if let value = minusHoursText { dictionary[SerializationKeys.minusHoursText] = value }
    return dictionary
  }

}
