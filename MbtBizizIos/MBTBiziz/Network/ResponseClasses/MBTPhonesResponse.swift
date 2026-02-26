//
//  MBTPhonesResponse.swift
//
//  Created by Serkut Yegin on 8.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTPhonesResponse: MarshalResponse {
    // MARK: Declaration for string constants to be used to decode and also serialize.
    private struct SerializationKeys {
      static let phones = "phones"
    }

    // MARK: Properties
    public var phones: [MBTPhone]?
    
    // MARK: Marshal Initializers

    /// Map a JSON object to this class using Marshal.
    ///
    /// - parameter object: A mapping from ObjectMapper
    public required init(object: MarshaledObject) {
        try! super.init(object: object)
        phones = try? object.value(for: SerializationKeys.phones)
    }

    /// Generates description of the object in the form of a NSDictionary.
    ///
    /// - returns: A Key value pair containing all valid values in the object.
    public func dictionaryRepresentation() -> [String: Any] {
        var dictionary: [String: Any] = [:]
        if let value = phones { dictionary[SerializationKeys.phones] = value.map { $0.dictionaryRepresentation() } }
        return dictionary
    }
}

public class MBTPhone: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let name = "name"
    static let details = "details"
  }

  // MARK: Properties
  public var id: Int?
  public var name: String?
  public var details: [MBTPhoneDetail]?
    
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

public class MBTPhoneDetail: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let unit = "unit"
    static let notes = "notes"
    static let internal = "internal"
  }

  // MARK: Properties
  public var id: Int?
  public var unit: String?
  public var notes: String?
  public var internal = String?
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
    if let value = unit { dictionary[SerializationKeys.unit] = value }
    if let value = notes { dictionary[SerializationKeys.notes] = value }
    if let value = internal { dictionary[SerializationKeys.internal] = value }
    return dictionary
  }
}
