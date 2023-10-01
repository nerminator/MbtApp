//
//  MBTNewsResponse.swift
//
//  Created by Serkut Yegin on 6.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTNewsResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let birthdayCount = "birthdayCount"
    static let newsList = "newsList"
  }

  // MARK: Properties
  public var birthdayCount: Int?
  public var newsList: [MBTNewsNewsList]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    birthdayCount = try? object.value(for: SerializationKeys.birthdayCount)
    newsList = try? object.value(for: SerializationKeys.newsList)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = birthdayCount { dictionary[SerializationKeys.birthdayCount] = value }
    if let value = newsList { dictionary[SerializationKeys.newsList] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
