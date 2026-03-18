//
//  MBTLocationItem.swift
//
//  Created by Serkut Yegin on 26.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTLocationItem: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let name = "name"
    static let buildingList = "buildingList"
    static let emergencyPoints = "emergencyPoints"
    static let id = "id"
    static let image = "image"
    static let isDefault = "isDefault"
  }

  // MARK: Properties
  public var name: String?
  public var buildingList: [MBTLocationBuildingList]?
  public var emergencyPoints: [MBTLocationBuildingList]?
  public var id: Int?
  public var image: String?
  public var isDefault : Bool? = false

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    name = try? object.value(for: SerializationKeys.name)
    
    buildingList = try? object.value(for: SerializationKeys.buildingList)
    emergencyPoints = try? object.value(for: SerializationKeys.emergencyPoints)
    id = try? object.value(for: SerializationKeys.id)
    image = try? object.value(for: SerializationKeys.image)
    isDefault = try? object.value(for: SerializationKeys.isDefault)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    dictionary[SerializationKeys.isDefault] = isDefault
    if let value = name { dictionary[SerializationKeys.name] = value }
    if let value = buildingList { dictionary[SerializationKeys.buildingList] = value.map { $0.dictionaryRepresentation() } }
    if let value = emergencyPoints { dictionary[SerializationKeys.emergencyPoints] = value.map { $0.dictionaryRepresentation() } }
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = image { dictionary[SerializationKeys.image] = value }
    return dictionary
  }

}
