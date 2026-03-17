//
//  MBTFoodMenuDensityInfo.swift
//
//  Created by Serkut Yegin on 12.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTFoodMenuDensityInfo: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let percent = "percent"
    static let text = "text"
    static let locationNumber = "locationNumber"
    static let locationName = "locationText"
  }

  // MARK: Properties
  public var percent: Int?
  public var text: String?
  public var locationNumber: Int?
  public var locationName: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    percent = try? object.value(for: SerializationKeys.percent)
    text = try? object.value(for: SerializationKeys.text)
    locationNumber = try? object.value(for: SerializationKeys.locationNumber)
    locationName = try? object.value(for: SerializationKeys.locationName)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = percent { dictionary[SerializationKeys.percent] = value }
    if let value = text { dictionary[SerializationKeys.text] = value }
    if let value = locationNumber { dictionary[SerializationKeys.locationNumber] = value }
    if let value = locationName { dictionary[SerializationKeys.locationName] = value }
    return dictionary
  }

}
