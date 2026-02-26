//
//  MBTSocialMediaResponse.swift
//  MBTBiziz
//
//  Created by Nermy on 28.12.2023.
//  Copyright © 2023 Serkut Yegin. All rights reserved.
//

import Foundation
import Marshal

public class MBTSocialMediaResponse: MarshalResponse {
    // MARK: Declaration for string constants to be used to decode and also serialize.
    private struct SerializationKeys {
      static let medias = "medias"
    }

    // MARK: Properties
    public var medias: [MBTMedia]?
    
    // MARK: Marshal Initializers

    /// Map a JSON object to this class using Marshal.
    ///
    /// - parameter object: A mapping from ObjectMapper
    public required init(object: MarshaledObject) {
        try! super.init(object: object)
        medias = try? object.value(for: SerializationKeys.medias)
    }

    /// Generates description of the object in the form of a NSDictionary.
    ///
    /// - returns: A Key value pair containing all valid values in the object.
    public func dictionaryRepresentation() -> [String: Any] {
        var dictionary: [String: Any] = [:]
        if let value = medias { dictionary[SerializationKeys.medias] = value.map { $0.dictionaryRepresentation() } }
        return dictionary
    }
}

public class MBTMedia: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let name = "name"
    static let details = "details"
  }

  // MARK: Properties
  public var id: Int?
  public var name: String?
  public var details: [MBTMediaDetail]?
    
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

public class MBTMediaDetail: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let id = "id"
    static let account = "account"
    static let url = "url"
  }

  // MARK: Properties
  public var id: Int?
  public var account: String?
  public var url: String?
    
  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    id = try? object.value(for: SerializationKeys.id)
    account = try? object.value(for: SerializationKeys.account)
    url = try? object.value(for: SerializationKeys.url)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = account { dictionary[SerializationKeys.account] = value }
    if let value = url { dictionary[SerializationKeys.url] = value }
    return dictionary
  }
}
