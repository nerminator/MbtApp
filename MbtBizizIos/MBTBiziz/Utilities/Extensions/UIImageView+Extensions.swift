//
//  UIImageView+Extensions.swift
//  MBT
//
//  Created by Serkut Yegin on 20.06.2017.
//  Copyright © 2017 Daimler AG. All rights reserved.
//

import UIKit
import AlamofireImage

extension UIImageView {
    
    func mbtSetImage(urlStr:String?, placeHolder:UIImage? = nil, completion:((_ image : UIImage?)->())? = nil) {
        image = placeHolder
        contentMode = .scaleAspectFit
        guard let urlStr = urlStr, let imageUrl = URL(string: urlStr) else { completion?(nil); return }
        af_setImage(withURL: imageUrl, placeholderImage: placeHolder, completion: { [weak self] (response) in
            if response.result.isSuccess, let image = response.result.value {
                self?.image = image
                self?.contentMode = .scaleAspectFill
                completion?(image)
            } else {
                self?.image = placeHolder
                self?.contentMode = .scaleAspectFit
                completion?(nil)
            }
        })
    }

}
