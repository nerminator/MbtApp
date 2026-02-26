//
//  MBTLocationBuildingList.swift
//
//  Created by Serkut Yegin on 26.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal
import MapKit

public class MBTLocationBuildingList: NSObject, Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let name = "name"
    static let latitude = "latitude"
    static let meetingRoomList = "meetingRoomList"
    static let id = "id"
    static let longitude = "longitude"
    static let order = "order"
    static let shortName = "shortName"
  }

  // MARK: Properties
  public var name: String?
  public var latitude: String?
  public var meetingRoomList: [MBTLocationMeetingRoomList]?
  public var id: Int?
  public var longitude: String?
  public var order: Int?
  public var isEmergency: Bool = false;
  public var shortName: String?
    
  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    name = try? object.value(for: SerializationKeys.name)
    latitude = try? object.value(for: SerializationKeys.latitude)
    meetingRoomList = try? object.value(for: SerializationKeys.meetingRoomList)
    id = try? object.value(for: SerializationKeys.id)
    longitude = try? object.value(for: SerializationKeys.longitude)
    order = try? object.value(for: SerializationKeys.order)
    shortName =  try? object.value(for: SerializationKeys.shortName)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = name { dictionary[SerializationKeys.name] = value }
    if let value = latitude { dictionary[SerializationKeys.latitude] = value }
    if let value = meetingRoomList { dictionary[SerializationKeys.meetingRoomList] = value.map { $0.dictionaryRepresentation() } }
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = longitude { dictionary[SerializationKeys.longitude] = value }
    if let value = order { dictionary[SerializationKeys.order] = value }
    if let value = shortName { dictionary[SerializationKeys.shortName] = value }
    return dictionary
  }

}

extension MBTLocationBuildingList : MKAnnotation {
    
    public var coordinate: CLLocationCoordinate2D {
        guard let latitudeStr = latitude, let latitude = Double(latitudeStr), let longitudeStr = longitude, let longitude = Double(longitudeStr) else { return kCLLocationCoordinate2DInvalid }
        return CLLocationCoordinate2DMake(latitude, longitude)
    }
}
