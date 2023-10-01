//
//  MBTNewsNewsList.swift
//
//  Created by Serkut Yegin on 6.05.2018
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

enum NewsType : Int {
    case hepsi = 1
    case etkinlik, indirim, veda, vefat, diger, duyurular,linkler,baglantilar
    
    var title : String {
        switch self {
        case .hepsi: return "TXT_LOGIN_NEWS_TYPE1".localized()
        case .etkinlik: return "TXT_LOGIN_NEWS_TYPE2".localized()
        case .indirim: return "TXT_LOGIN_NEWS_TYPE3".localized()
        case .veda: return "TXT_LOGIN_NEWS_TYPE4".localized()
        case .vefat: return "TXT_LOGIN_NEWS_TYPE5".localized()
        case .diger: return "TXT_LOGIN_NEWS_TYPE6".localized()
        case .duyurular: return "TXT_LOGIN_NEWS_TYPE7".localized()
        case .linkler: return "TXT_LOGIN_NEWS_TYPE8".localized()
        case .baglantilar: return "TXT_LOGIN_NEWS_TYPE9".localized()
        }
    }
    
    static var newsTypeList : [NewsType] = [.hepsi,.etkinlik,.duyurular,.linkler,.baglantilar,.indirim,.diger]
}

enum DiscountType : Int {
    case tumu = 1
    case egitim,eTicaret,evDekorasyon,gastronomi,gayrimenkul,giyim,otomotiv,saglik,spor,temizlik,turizm,diger, teknoloji
    
    var title : String {
        switch self {
        case .tumu: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE1".localized()
        case .egitim: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE2".localized()
        case .eTicaret: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE3".localized()
        case .evDekorasyon: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE4".localized()
        case .gastronomi: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE5".localized()
        case .gayrimenkul: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE6".localized()
        case .giyim: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE7".localized()
        case .otomotiv: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE8".localized()
        case .saglik: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE9".localized()
        case .spor: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE10".localized()
        case .temizlik: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE11".localized()
        case .turizm: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE12".localized()
        case .diger: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE13".localized()
        case .teknoloji: return "TXT_LOGIN_NEWS_DISCOUNT_TYPE14".localized()
        }
    }
    
    static var discountList : [DiscountType] = [.tumu,.egitim,.eTicaret,.evDekorasyon,.gastronomi,.gayrimenkul,.giyim,.otomotiv,.saglik,.turizm,.teknoloji,.diger]
}

protocol NewsProtocol {
    var discountTypeEnum : DiscountType? { get }
    var typeEnum : NewsType? { get }
    var identifier : Int? { get }
}

extension MBTNewsNewsList : NewsProtocol {
    
    var discountTypeEnum : DiscountType? {
        guard let discountType = discountType else { return nil }
        return DiscountType(rawValue: discountType)
    }
    
    var typeEnum : NewsType? {
        guard let type = type else { return nil }
        return NewsType(rawValue: type)
    }
    
    var identifier: Int? {
        return id
    }
}

public class MBTNewsNewsList: Unmarshaling {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let discountType = "discountType"
    static let id = "id"
    static let image = "image"
    static let text = "listText"
    static let monthName = "monthName"
    static let type = "type"
    static let typeTitle = "typeTitle"
    static let url = "url"
    static let phone = "phone"
  }

  // MARK: Properties
  public var discountType: Int?
  public var id: Int?
  public var image: String?
  public var text: String?
  public var monthName: String?
  public var type: Int?
  public var typeTitle: String?
  public var url: String?
  public var phone: String?
    
  // MARK: Marshal Initializers

  /// Map a JSON object to this class using Marshal.
  ///
  /// - parameter object: A mapping from ObjectMapper
  public required init(object: MarshaledObject) {
    discountType = try? object.value(for: SerializationKeys.discountType)
    id = try? object.value(for: SerializationKeys.id)
    image = try? object.value(for: SerializationKeys.image)
    text = try? object.value(for: SerializationKeys.text)
    monthName = try? object.value(for: SerializationKeys.monthName)
    type = try? object.value(for: SerializationKeys.type)
    typeTitle = try? object.value(for: SerializationKeys.typeTitle)
    url = try? object.value(for: SerializationKeys.url)
    phone = try? object.value(for: SerializationKeys.phone)
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = discountType { dictionary[SerializationKeys.discountType] = value }
    if let value = id { dictionary[SerializationKeys.id] = value }
    if let value = image { dictionary[SerializationKeys.image] = value }
    if let value = text { dictionary[SerializationKeys.text] = value }
    if let value = monthName { dictionary[SerializationKeys.monthName] = value }
    if let value = type { dictionary[SerializationKeys.type] = value }
    if let value = typeTitle { dictionary[SerializationKeys.typeTitle] = value }
    if let value = url { dictionary[SerializationKeys.url] = value }
    if let value = phone { dictionary[SerializationKeys.phone] = value }
    return dictionary
  }

}
