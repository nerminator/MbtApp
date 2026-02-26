//
//  MBTSettingsResponse.swift
//
//  Created by Serkut Yegin on 13.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTSettingsResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let notificationSettingList = "notificationSettingList"
  }

  // MARK: Properties
  public var notificationSettingList: [MBTSettingsNotificationSettingList]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    notificationSettingList = try? object.value(for: SerializationKeys.notificationSettingList)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = notificationSettingList { dictionary[SerializationKeys.notificationSettingList] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
