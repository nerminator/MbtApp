//
//  MBTTransportationOptionsResponse.swift
//
//  Created by Serkut Yegin on 9.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTTransportationOptionsResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let companyLocationList = "companyLocationList"
    static let typeList = "typeList"
  }

  // MARK: Properties
  public var companyLocationList: [MBTTransportationOptionsCompanyLocationList]?
  public var typeList: [MBTTransportationOptionsTypeList]?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    companyLocationList = try? object.value(for: SerializationKeys.companyLocationList)
    typeList = try? object.value(for: SerializationKeys.typeList)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = companyLocationList { dictionary[SerializationKeys.companyLocationList] = value.map { $0.dictionaryRepresentation() } }
    if let value = typeList { dictionary[SerializationKeys.typeList] = value.map { $0.dictionaryRepresentation() } }
    return dictionary
  }

}
