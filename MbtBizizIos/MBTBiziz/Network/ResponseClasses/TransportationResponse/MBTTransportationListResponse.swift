//
//  MBTTransportationListResponse.swift
//
//  Created by Serkut Yegin on 9.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTTransportationListResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let toCompanyList = "toCompanyList"
    static let companyLocationId = "companyLocationId"
    static let type = "type"
    static let fromCompanyList = "fromCompanyList"
  }

  // MARK: Properties
  public var toCompanyList: [MBTTransportationListToCompanyList]?
  public var companyLocationId: Int?
  public var type: Int?
  public var fromCompanyList: [MBTTransportationListToCompanyList]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    toCompanyList = try? object.value(for: SerializationKeys.toCompanyList)
    companyLocationId = try? object.value(for: SerializationKeys.companyLocationId)
    type = try? object.value(for: SerializationKeys.type)
    fromCompanyList = try? object.value(for: SerializationKeys.fromCompanyList)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = toCompanyList { dictionary[SerializationKeys.toCompanyList] = value.map { $0.dictionaryRepresentation() } }
    if let value = companyLocationId { dictionary[SerializationKeys.companyLocationId] = value }
    if let value = type { dictionary[SerializationKeys.type] = value }
    if let value = fromCompanyList { dictionary[SerializationKeys.fromCompanyList] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
