//
//  MBTCaptchaResponse.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 25.01.2020.
//  Copyright © 2020 Serkut Yegin. All rights reserved.
//

import Foundation
import Marshal

public class MBTCaptchaResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let token = "token"
    static let image = "image"
  }

  // MARK: Properties
  public var token: String?
  public var image: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    token = try? object.value(for: SerializationKeys.token)
    image = try? object.value(for: SerializationKeys.image)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = token { dictionary[SerializationKeys.token] = value }
    if let value = image { dictionary[SerializationKeys.image] = value }
    return dictionary
  }

}
