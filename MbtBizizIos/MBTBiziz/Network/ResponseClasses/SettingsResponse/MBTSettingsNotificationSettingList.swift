//
//  MBTSettingsNotificationSettingList.swift
//
//  Created by Serkut Yegin on 13.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

extension MBTSettingsNotificationSettingList {
    
    var isSelected : Bool {
        guard let value = value else { return false }
        return value > 0
    }
}

public class MBTSettingsNotificationSettingList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let type = "type"
    static let title = "title"
    static let subtitle = "subtitle"
    static let value = "value"
  }

  // MARK: Properties
  public var type: Int?
  public var title: String?
  public var subtitle: String?
  public var value: Int?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    type = try? object.value(for: SerializationKeys.type)
    title = try? object.value(for: SerializationKeys.title)
    subtitle = try? object.value(for: SerializationKeys.subtitle)
    value = try? object.value(for: SerializationKeys.value)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = type { dictionary[SerializationKeys.type] = value }
    if let value = title { dictionary[SerializationKeys.title] = value }
    if let value = subtitle { dictionary[SerializationKeys.subtitle] = value }
    if let value = value { dictionary[SerializationKeys.value] = value }
    return dictionary
  }

}
