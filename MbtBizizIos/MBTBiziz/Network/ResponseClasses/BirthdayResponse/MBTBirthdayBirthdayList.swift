//
//  MBTBirthdayBirthdayList.swift
//
//  Created by Serkut Yegin on 8.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTBirthdayBirthdayList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let name = "name"
    static let title = "title"
  }

  // MARK: Properties
  public var name: String?
  public var title: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    name = try? object.value(for: SerializationKeys.name)
    title = try? object.value(for: SerializationKeys.title)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = name { dictionary[SerializationKeys.name] = value }
    if let value = title { dictionary[SerializationKeys.title] = value }
    return dictionary
  }

}
