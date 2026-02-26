//
//  MBTTransportationListShuttleList.swift
//
//  Created by Serkut Yegin on 9.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public struct MBTTransportationListShuttleList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let departureTime = "departureTime"
    static let name = "name"
    static let driverInfo = "driverInfo"
    static let id = "id"
    static let stopList = "stopList"
    static let arrivalTime = "arrivalTime"
  }

  // MARK: Properties
  public var departureTime: String?
  public var name: String?
  public var driverInfo: MBTTransportationListDriverInfo?
  public var id: Int?
  public var stopList: [MBTTransportationListStopList]?
  public var arrivalTime: String?
    
    fileprivate var isAppendedCompanyName : Bool = false

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public init(object: MarshaledObject) {
    departureTime = try? object.value(for: SerializationKeys.departureTime)
    name = try? object.value(for: SerializationKeys.name)
    driverInfo = try? object.value(for: SerializationKeys.driverInfo)
    id = try? object.value(for: SerializationKeys.id)
    stopList = try? object.value(for: SerializationKeys.stopList)
    arrivalTime = try? object.value(for: SerializationKeys.arrivalTime)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = departureTime { dictionary[SerializationKeys.departureTime] = value }
    if let value = name { dictionary[SerializationKeys.name] = value }
    if let value = driverInfo { dictionary[SerializationKeys.driverInfo] = value.dictionaryRepresentation() }
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = stopList { dictionary[SerializationKeys.stopList] = value.map { $0.dictionaryRepresentation() } }
    if let value = arrivalTime { dictionary[SerializationKeys.arrivalTime] = value }
    return dictionary
  }
    
    mutating func appendCompanyName(_ companyName: String?, isArrival:Bool) {
        if !isAppendedCompanyName {
            if isArrival {
                stopList?.append(MBTTransportationListStopList.init(name: companyName?.uppercased(with: .mbtLocale)))
            } else {
                stopList?.insert(MBTTransportationListStopList.init(name: companyName?.uppercased(with: .mbtLocale)), at: 0)
            }
            isAppendedCompanyName = true
        }
    }

}
