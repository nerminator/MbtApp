//
//  MBTClubsResponse.swift
//
//  Created by Serkut Yegin on 8.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTClubsResponse: MarshalResponse {
    // MARK: Declaration for string constants to be used to decode and also serialize.
    private struct SerializationKeys {
      static let clubs = "clubs"
    }

    // MARK: Properties
    public var clubs: [MBTClub]?
    
    // MARK: Marshal Initializers

    /// Map a JSON object to this class using Marshal.
    ///
    /// - parameter object: A mapping from ObjectMapper
    public required init(object: MarshaledObject) {
        try! super.init(object: object)
        clubs = try? object.value(for: SerializationKeys.clubs)
    }

    /// Generates description of the object in the form of a NSDictionary.
    ///
    /// - returns: A Key value pair containing all valid values in the object.
    public func dictionaryRepresentation() -> [String: Any] {
        var dictionary: [String: Any] = [:]
        if let value = clubs { dictionary[SerializationKeys.clubs] = value.map { $0.dictionaryRepresentation() } }
        return dictionary
    }
}

public class MBTClub: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let name = "name"
    static let details = "details"
  }

  // MARK: Properties
  public var id: Int?
  public var name: String?
  public var details: [MBTClubDetail]?
    
  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    id = try? object.value(for: SerializationKeys.id)
    name = try? object.value(for: SerializationKeys.name)
    details = try? object.value(for: SerializationKeys.details)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = name { dictionary[SerializationKeys.name] = value }
    if let value = details { dictionary[SerializationKeys.details] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }
}

public class MBTClubDetail: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let responsible = "responsible"
    static let contact = "contact"
  }

  // MARK: Properties
  public var id: Int?
  public var responsible: String?
  public var contact: String?
    
  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    id = try? object.value(for: SerializationKeys.id)
    responsible = try? object.value(for: SerializationKeys.responsible)
    contact = try? object.value(for: SerializationKeys.contact)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = responsible { dictionary[SerializationKeys.responsible] = value }
    if let value = contact { dictionary[SerializationKeys.contact] = value }
    return dictionary
  }
}
