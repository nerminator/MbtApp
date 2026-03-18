//
//  MBTBirthdayResponse.swift
//
//  Created by Serkut Yegin on 8.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTBirthdayResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let dateInfo = "dateInfo"
    static let birthdayList = "birthdayList"
  }

  // MARK: Properties
  public var dateInfo: String?
  public var birthdayList: [MBTBirthdayBirthdayList]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    dateInfo = try? object.value(for: SerializationKeys.dateInfo)
    birthdayList = try? object.value(for: SerializationKeys.birthdayList)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = dateInfo { dictionary[SerializationKeys.dateInfo] = value }
    if let value = birthdayList { dictionary[SerializationKeys.birthdayList] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
