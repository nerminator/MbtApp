//
//  MBTProfileResponse.swift
//
//  Created by Serkut Yegin on 26.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTProfileResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let organizationUnit = "organizationUnit"
    static let officeLocation = "officeLocation"
    static let nameSurname = "nameSurname"
    static let manager = "manager"
    static let title = "title"
    static let employeeType = "employeeType"
    static let registerNumber = "registerNumber"
    static let workHoursText = "workHoursText"
    static let workHoursAvailable = "workHoursAvailable"
    static let yearlyWorkHoursText = "yearlyWorkHoursText"
    static let monthlyWorkHoursText = "monthlyWorkHoursText"
  }

  // MARK: Properties
  public var organizationUnit: String?
  public var officeLocation: String?
  public var nameSurname: String?
  public var manager: String?
  public var title: String?
  public var employeeType: Int?
  public var registerNumber: String?
  public var workHoursText: String?
  public var workHoursAvailable: Bool?
    public var yearlyWorkHoursText: String?
    public var monthlyWorkHoursText: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    organizationUnit = try? object.value(for: SerializationKeys.organizationUnit)
    officeLocation = try? object.value(for: SerializationKeys.officeLocation)
    nameSurname = try? object.value(for: SerializationKeys.nameSurname)
    manager = try? object.value(for: SerializationKeys.manager)
    title = try? object.value(for: SerializationKeys.title)
    employeeType = try? object.value(for: SerializationKeys.employeeType)
    registerNumber = try? object.value(for: SerializationKeys.registerNumber)
    workHoursText = try? object.value(for: SerializationKeys.workHoursText)
    workHoursAvailable = try? object.value(for: SerializationKeys.workHoursAvailable)
    yearlyWorkHoursText = try? object.value(for: SerializationKeys.yearlyWorkHoursText)
    monthlyWorkHoursText = try? object.value(for: SerializationKeys.monthlyWorkHoursText)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = organizationUnit { dictionary[SerializationKeys.organizationUnit] = value }
    if let value = officeLocation { dictionary[SerializationKeys.officeLocation] = value }
    if let value = nameSurname { dictionary[SerializationKeys.nameSurname] = value }
    if let value = manager { dictionary[SerializationKeys.manager] = value }
    if let value = title { dictionary[SerializationKeys.title] = value }
    if let value = employeeType { dictionary[SerializationKeys.employeeType] = value }
    if let value = registerNumber { dictionary[SerializationKeys.registerNumber] = value }
    if let value = workHoursText { dictionary[SerializationKeys.workHoursText] = value }
    if let value = workHoursAvailable { dictionary[SerializationKeys.workHoursAvailable] = value }
    if let value = yearlyWorkHoursText { dictionary[SerializationKeys.yearlyWorkHoursText] = value }
    if let value = monthlyWorkHoursText { dictionary[SerializationKeys.monthlyWorkHoursText] = value }
    return dictionary
  }

}
