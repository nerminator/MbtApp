//
//  MBTNewsDetailResponse.swift
//
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

public class MBTNewsDetailResponse: MarshalResponse {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let discountType = "discountType"
    static let image = "image"
    static let images = "images"
    static let pdfs = "pdfs"
    static let text = "text"
    static let dateInfo = "dateInfo"
    static let title = "title"
    static let subText = "subText"
    static let type = "type"
    static let subTitle = "subTitle"
    static let url = "url"
    static let discountCodeType = "discountCodeType"
    static let discountCodeAll = "discountCodeAll"
  }

  // MARK: Properties
  public var discountType: Int?
  public var image: String?
  public var images: [String]?
  public var pdfs: [MBTNewsPdf]?
  public var text: String?
  public var dateInfo: String?
  public var title: String?
  public var subText: String?
  public var type: Int?
  public var subTitle: String?
  public var url: String?
  public var discountCodeType: Int?
  public var discountCodeAll: String?
  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    try! super.init(object: object)
    discountType = try? object.value(for: SerializationKeys.discountType)
    image = try? object.value(for: SerializationKeys.image)
    images = try? object.value(for: SerializationKeys.images)
    pdfs = try? object.value(for: SerializationKeys.pdfs)
    text = try? object.value(for: SerializationKeys.text)
    dateInfo = try? object.value(for: SerializationKeys.dateInfo)
    title = try? object.value(for: SerializationKeys.title)
    subText = try? object.value(for: SerializationKeys.subText)
    type = try? object.value(for: SerializationKeys.type)
    subTitle = try? object.value(for: SerializationKeys.subTitle)
    url = try? object.value(for: SerializationKeys.url)
    discountCodeType = try? object.value(for: SerializationKeys.discountCodeType)
    discountCodeAll = try? object.value(for: SerializationKeys.discountCodeAll)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = discountType { dictionary[SerializationKeys.discountType] = value }
    if let value = image { dictionary[SerializationKeys.image] = value }
    if let value = images { dictionary[SerializationKeys.images] = value }
    if let value = pdfs { dictionary[SerializationKeys.pdfs] = value }
    if let value = text { dictionary[SerializationKeys.text] = value }
    if let value = dateInfo { dictionary[SerializationKeys.dateInfo] = value }
    if let value = title { dictionary[SerializationKeys.title] = value }
    if let value = subText { dictionary[SerializationKeys.subText] = value }
    if let value = type { dictionary[SerializationKeys.type] = value }
    if let value = subTitle { dictionary[SerializationKeys.subTitle] = value }
    if let value = url { dictionary[SerializationKeys.url] = value }
    if let value = discountCodeType { dictionary[SerializationKeys.discountCodeType] = value }
    if let value = discountCodeAll { dictionary[SerializationKeys.discountCodeAll] = value }
    return dictionary
  }

}
