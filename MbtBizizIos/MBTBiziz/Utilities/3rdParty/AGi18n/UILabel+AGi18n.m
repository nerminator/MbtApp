//
//  UILabel+AGi18n.m
//  AGi18n
//
//  Created by Angel Garcia on 3/13/13.
//  Copyright (c) 2013 angelolloqui.com. All rights reserved.
//

#import "UILabel+AGi18n.h"
#import "MBTBiziz-Swift.h"

@implementation UILabel (AGi18n)

- (void)localizeFromNib {
    //Replace text with localizable version
    if (self.text.length > 0) {
        self.text = [[MBTLanguageManager sharedManager] getLocalizedStringWithKey:self.text];
    }
}


@end
