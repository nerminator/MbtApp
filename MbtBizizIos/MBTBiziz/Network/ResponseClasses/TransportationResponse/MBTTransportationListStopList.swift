//
//  MBTTransportationListStopList.swift
//
//  Created by Serkut Yegin on 9.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public struct MBTTransportationListStopList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let name = "name"
  }

  // MARK: Properties
  public var id: Int?
  public var name: String?
  public var shuttleId : Int?
  public var shuttleName : String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public init(object: MarshaledObject) {
    id = try? object.value(for: SerializationKeys.id)
    name = try? object.value(for: SerializationKeys.name)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = name { dictionary[SerializationKeys.name] = value }
    return dictionary
  }
    
    init(name : String?) {
        self.name = name
    }
    
    mutating func appendShuttleInfo(shuttleItem : MBTTransportationListShuttleList?) -> MBTTransportationListStopList {
        self.shuttleId = shuttleItem?.id
        self.shuttleName = shuttleItem?.name
        return self
    }
    
    init(from searchItem : MBTTransportationSearchItem) {
        self.name = searchItem.name
        self.id = searchItem.id
        self.shuttleName = searchItem.shuttleName
        self.shuttleId = searchItem.shuttleId
    }

}
