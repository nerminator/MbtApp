//
//  Parsable.swift
//
//  Created by Serkut Yegin.
//

import UIKit
import Moya
import Result
import Marshal

typealias MoyaResult = Result<Moya.Response, Moya.MoyaError>

public class ParseUtils {
    
    static func mapJSON(_ data: Data) -> (Any?, NSError?) {
        var error: NSError?
        var json: Any?
        do {
            json = try JSONSerialization.jsonObject(with: data)
        } catch let error1 as NSError {
            error = error1
            json = nil
        }
        return (json, error)
    }
}

protocol Parsable {
    static func parse(_ moyaResponse : Response) -> Self?
}
public class DecodableResponse: Decodable {
    
    required public init(from decoder: Decoder) throws {
    }
}

public class MarshalResponse : Unmarshaling {
    
    required public init(object: MarshaledObject) throws {
    }
    
    init() { }
}

extension DecodableResponse : Parsable {
    static func parse(_ moyaResponse: Response) -> Self? {
        let rawDictionary = moyaResponse.getRawDictionary()?[KEY_PAYLOAD_MESSAGE] as? [String : Any]
        do {
            let jsonData = try JSONSerialization.data(withJSONObject: rawDictionary ?? [:])
            let response = Response(statusCode: moyaResponse.statusCode, data: jsonData)
            let parsed = try response.map(to: self)
            return parsed
        } catch let error {
            print(error)
        }
        return nil
    }
}

extension MarshalResponse : Parsable {
    static func parse(_ moyaResponse : Response) -> Self? {
        let rawDictionary = moyaResponse.getRawDictionary()?[KEY_PAYLOAD_MESSAGE] as? [String : Any]
        do {
            let parsed = try self.init(object: rawDictionary ?? [:])
            return parsed
        } catch {}
        return nil
    }
}

extension Int : Parsable {
    static func parse(_ moyaResponse: Response) -> Int? { return moyaResponse.getRawDictionary()?[KEY_PAYLOAD_MESSAGE] as? Int }
}
extension Double : Parsable {
    static func parse(_ moyaResponse: Response) -> Double? { return moyaResponse.getRawDictionary()?[KEY_PAYLOAD_MESSAGE] as? Double }
}
extension String : Parsable {
    static func parse(_ moyaResponse: Response) -> String? { return moyaResponse.getRawDictionary()?[KEY_PAYLOAD_MESSAGE] as? String }
}

extension Array : Parsable where Element : MarshalResponse {
    
    static func parse(_ moyaResponse: Response) -> Array<Element>? {
        guard let rawArray = moyaResponse.getRawDictionary()?[KEY_PAYLOAD_MESSAGE] as? [Any] else { return nil }
        var returnObject = [Element]()
        rawArray.forEach({ (rawObject) in
            if let object = rawObject as? [String : Any] {
                do {
                    let parsed = try Element.init(object: object)
                    returnObject.append(parsed)
                } catch {}
            }
        })
        return returnObject
    }
}

struct WSResponse<T : Parsable> {
    typealias WSMapCompletion = (_ response : T?)->()
    typealias WSWrappedSuccessCompletion = (WSResponse<T>) -> Void
    typealias WSSuccessCompletion = (T?) -> Void
    var statusCode : WSStatusCode
    var errorMessage : String?
    var data : T?
    var rawDictionary : [String : Any]?
    
    var isSuccess : Bool {
        return statusCode == .success
    }
    
    var hasAuthorizationError : Bool {
        return statusCode == .authorizationError
    }
}

public extension Response {
    
    public func map<T: Decodable>(
        to type: T.Type,
        dataDecodingStategy: JSONDecoder.DataDecodingStrategy = .base64,
        dateDecodingStrategy: JSONDecoder.DateDecodingStrategy = .deferredToDate,
        nonConformingFloatDecodingStrategy: JSONDecoder.NonConformingFloatDecodingStrategy = .throw
        ) throws -> T {
        let decoder = JSONDecoder()
        decoder.dataDecodingStrategy = dataDecodingStategy
        decoder.dateDecodingStrategy = dateDecodingStrategy
        decoder.nonConformingFloatDecodingStrategy = nonConformingFloatDecodingStrategy
        return try decoder.decode(type, from: data)
    }
    
    public func getRawDictionary() -> [String : Any]? {
        let (mappedJSON, error): (Any?, NSError?) = ParseUtils.mapJSON(data)
        if error == nil, let dict = mappedJSON as? [String: Any] {
            return dict
        }
        return nil
    }
}
