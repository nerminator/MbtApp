//
//  MBTNotificationListItem.swift
//
//  Created by Serkut Yegin on 14.06.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

extension MBTNotificationListItem : NewsProtocol {
    
    var isSeen : Bool {
        guard let seen = seen else { return true }
        return seen
    }
    
    var typeEnum : NewsType? {
        guard let type = type else { return nil }
        return NewsType(rawValue: type)
    }
    
    var discountTypeEnum : DiscountType? {
        guard let discountType = discountType else { return nil }
        return DiscountType(rawValue: discountType)
    }
    
    var identifier: Int? {
        return newsId
    }
}

public class MBTNotificationListItem: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let discountType = "discountType"
    static let listText = "listText"
    static let id = "id"
    static let image = "image"
    static let type = "type"
    static let newsId = "newsId"
    static let seen = "seen"
    static let notificationTime = "notificationTime"
  }

  // MARK: Properties
  public var discountType: Int?
  public var listText: String?
  public var id: Int?
  public var image: String?
  public var type: Int?
  public var newsId: Int?
  public var seen: Bool? = false
  public var notificationTime: String?

  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object:object)
    discountType = try? object.value(for: SerializationKeys.discountType)
    listText = try? object.value(for: SerializationKeys.listText)
    id = try? object.value(for: SerializationKeys.id)
    image = try? object.value(for: SerializationKeys.image)
    type = try? object.value(for: SerializationKeys.type)
    newsId = try? object.value(for: SerializationKeys.newsId)
    seen = try? object.value(for: SerializationKeys.seen)
    notificationTime = try? object.value(for: SerializationKeys.notificationTime)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    dictionary[SerializationKeys.seen] = seen
    if let value = discountType { dictionary[SerializationKeys.discountType] = value }
    if let value = listText { dictionary[SerializationKeys.listText] = value }
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = image { dictionary[SerializationKeys.image] = value }
    if let value = type { dictionary[SerializationKeys.type] = value }
    if let value = newsId { dictionary[SerializationKeys.newsId] = value }
    if let value = notificationTime { dictionary[SerializationKeys.notificationTime] = value }
    return dictionary
  }

}
