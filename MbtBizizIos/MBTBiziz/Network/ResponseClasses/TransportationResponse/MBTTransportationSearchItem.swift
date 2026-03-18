//
//  MBTTransportationSearchItem.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 15.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import Foundation
import Marshal

extension Array where Element == MBTTransportationSearchItem {
    
    var converted : [MBTTransportationListStopList] {
        return self.map({ (searchItem) -> MBTTransportationListStopList in
            return MBTTransportationListStopList(from: searchItem)
        })
    }
    
    func save() {
        do {
            let data = try NSKeyedArchiver.archivedData(withRootObject: self, requiringSecureCoding: true)
            UserDefaults.standard.set(data, forKey: MBTConstants.UserPreference.TransportationSearchIdentifier)
        } catch {
            // İstersen logla
            // print("Archive error:", error)
        }
    }
    
    func hasItem(_ item : MBTTransportationSearchItem) -> Bool {
        for internalItem in self {
            if internalItem == item { return true }
        }
        return false
    }
}

extension MBTTransportationSearchItem {
    
    var title : String {
        if let name = name {
            if let shuttleName = shuttleName {
                return name + " (\(shuttleName))"
            }
            return name
        }
        return ""
    }
    
    class func load() -> [MBTTransportationSearchItem]  {
        guard let data = UserDefaults.standard.object(forKey: MBTConstants.UserPreference.TransportationSearchIdentifier) as? Data else {
            return []
        }

        do {
            if let items = try NSKeyedUnarchiver.unarchivedArrayOfObjects(ofClass: MBTTransportationSearchItem.self, from: data) {
                return items
            }
        } catch {
            // print("Unarchive error:", error)
        }

        return []
    }
}

extension Array where Element == MBTTransportationListStopList {
    var converted : [MBTTransportationSearchItem] {
        return self.map({ (stopListItem) -> MBTTransportationSearchItem in
            return MBTTransportationSearchItem(from: stopListItem)
        })
    }
    
    func save() {
        converted.save()
    }
}

extension MBTTransportationSearchItem {
    public static func == (lhs: MBTTransportationSearchItem, rhs: MBTTransportationSearchItem) -> Bool {
        guard let lShuttleId = lhs.shuttleId, let rShuttleId = rhs.shuttleId, let lName = lhs.name, let rName = rhs.name else { return false }
        return lShuttleId == rShuttleId && lName == rName
    }
}

extension MBTTransportationListStopList : Equatable {
    public static func == (lhs: MBTTransportationListStopList, rhs: MBTTransportationListStopList) -> Bool {
        guard let lShuttleId = lhs.shuttleId, let rShuttleId = rhs.shuttleId, let lName = lhs.name, let rName = rhs.name else { return false }
        return lShuttleId == rShuttleId && lName == rName
    }
}

public final class MBTTransportationSearchItem: NSObject, Unmarshaling, NSSecureCoding {

    // MARK: Declaration for string constants to be used to decode and also serialize.
    private struct SerializationKeys {
        static let name = "name"
        static let id = "id"
        static let shuttleName = "shuttleName"
        static let shuttleId = "shuttleId"
    }
    
    // MARK: Properties
    public var name: String?
    public var id: Int?
    public var shuttleName: String?
    public var shuttleId: Int?
    
    public static var supportsSecureCoding: Bool { true }
    
    // MARK: Marshal Initializers
    
    /// Map a JSON object to this class using Marshal.
    ///
    /// - parameter object: A mapping from ObjectMapper
    public required init(object: MarshaledObject) {
        name = try? object.value(for: SerializationKeys.name)
        id = try? object.value(for: SerializationKeys.id)
        shuttleName = try? object.value(for: SerializationKeys.shuttleName)
        shuttleId = try? object.value(for: SerializationKeys.shuttleId)
    }
    
    /// Generates description of the object in the form of a NSDictionary.
    ///
    /// - returns: A Key value pair containing all valid values in the object.
    public func dictionaryRepresentation() -> [String: Any] {
        var dictionary: [String: Any] = [:]
        if let value = name { dictionary[SerializationKeys.name] = value }
        if let value = id { dictionary[SerializationKeys.id] = value }
        if let value = shuttleName { dictionary[SerializationKeys.shuttleName] = value }
        if let value = shuttleId { dictionary[SerializationKeys.shuttleId] = value }
        return dictionary
    }
    
    // MARK: NSCoding Protocol
    required public init?(coder aDecoder: NSCoder) {
        super.init()

        self.name = aDecoder.decodeObject(of: NSString.self, forKey: SerializationKeys.name) as String?
        self.shuttleName = aDecoder.decodeObject(of: NSString.self, forKey: SerializationKeys.shuttleName) as String?

        // Int için ya decodeInteger + containsValue kontrolü,
        // ya da NSNumber allowlist:
        if aDecoder.containsValue(forKey: SerializationKeys.id) {
            self.id = aDecoder.decodeInteger(forKey: SerializationKeys.id)
        }
        if aDecoder.containsValue(forKey: SerializationKeys.shuttleId) {
            self.shuttleId = aDecoder.decodeInteger(forKey: SerializationKeys.shuttleId)
        }
    }
    
    public func encode(with aCoder: NSCoder) {
        aCoder.encode(name, forKey: SerializationKeys.name)
        aCoder.encode(id, forKey: SerializationKeys.id)
        aCoder.encode(shuttleName, forKey: SerializationKeys.shuttleName)
        aCoder.encode(shuttleId, forKey: SerializationKeys.shuttleId)
    }
    
    init(from stopListItem : MBTTransportationListStopList) {
        self.name = stopListItem.name
        self.id = stopListItem.id
        self.shuttleName = stopListItem.shuttleName
        self.shuttleId = stopListItem.shuttleId
    }
    
}
