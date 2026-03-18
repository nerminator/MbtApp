//
//  MBTWorkCalendarTypeList.swift
//  MBTBiziz
//
//  Created by Serkut Yegin on 2.06.2018.
//  Copyright © 2018 Daimler AG. All rights reserved.
//

import UIKit
import Marshal

public class MBTWorkCalendarTypeList: Unmarshaling {

    // MARK: Declaration for string constants to be used to decode and also serialize.
    private struct SerializationKeys {
        static let typeColor = "typeColor"
        static let typeName = "typeName"
    }
    
    // MARK: Properties
    public var typeColor: String?
    public var typeName: String?
    
    // MARK: Marshal Initializers
    
    /// Map a JSON object to this class using Marshal.
    ///
    /// - parameter object: A mapping from ObjectMapper
    public required init(object: MarshaledObject) {
        typeColor = try? object.value(for: SerializationKeys.typeColor)
        typeName = try? object.value(for: SerializationKeys.typeName)
    }
    
    /// Generates description of the object in the form of a NSDictionary.
    ///
    /// - returns: A Key value pair containing all valid values in the object.
    public func dictionaryRepresentation() -> [String: Any] {
        var dictionary: [String: Any] = [:]
        if let value = typeColor { dictionary[SerializationKeys.typeColor] = value }
        if let value = typeName { dictionary[SerializationKeys.typeName] = value }
        return dictionary
    }
}
