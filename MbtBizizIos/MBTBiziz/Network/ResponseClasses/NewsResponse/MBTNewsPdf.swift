//
//  MBTNewsPdf.swift
//  MBTBiziz
//
//  Created by Nermy on 21.10.2023.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTNewsPdf: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let pdf = "pdf"
    static let name = "name"
  }

  // MARK: Properties
  public var id: Int?
  public var pdf: String?
  public var name: String?


  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    id = try? object.value(for: SerializationKeys.id)
    pdf = try? object.value(for: SerializationKeys.pdf)
    name = try? object.value(for: SerializationKeys.name)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = pdf { dictionary[SerializationKeys.pdf] = value }
    if let value = name { dictionary[SerializationKeys.name] = value }
    return dictionary
  }

}
