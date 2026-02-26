//
//  MBTWorkCalendarInitialDayList.swift
//
//  Created by Serkut Yegin on 12.08.2018
//  Copyright (c) . All rights reserved.
//

import Foundation
import Marshal

public class MBTWorkCalendarInitialDayList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let dayText = "dayText"
    static let typeName = "typeName"
    static let typeColor = "typeColor"
  }

  // MARK: Properties
  public var dayText: String?
  public var typeName: String?
  public var typeColor: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    dayText = try? object.value(for: SerializationKeys.dayText)
    typeName = try? object.value(for: SerializationKeys.typeName)
    typeColor = try? object.value(for: SerializationKeys.typeColor)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = dayText { dictionary[SerializationKeys.dayText] = value }
    if let value = typeName { dictionary[SerializationKeys.typeName] = value }
    if let value = typeColor { dictionary[SerializationKeys.typeColor] = value }
    return dictionary
  }

}
