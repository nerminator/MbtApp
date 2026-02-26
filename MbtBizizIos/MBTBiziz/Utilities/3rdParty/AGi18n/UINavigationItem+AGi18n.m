//
//  UINavigationItem+AGi18n.m
//  AGi18n
//
//  Created by Angel Garcia on 3/17/13.
//  Copyright (c) 2013 angelolloqui.com. All rights reserved.
//

#import "UINavigationItem+AGi18n.h"
#import "MBTBiziz-Swift.h"

@implementation UINavigationItem (AGi18n)

- (void)localizeFromNib {
    //Replace text with localizable version
    if (self.title.length > 0) {
        self.title = [[MBTLanguageManager sharedManager] getLocalizedStringWithKey:self.title];
    }
    if (self.prompt.length > 0) {
        self.prompt = [[MBTLanguageManager sharedManager] getLocalizedStringWithKey:self.prompt];
    }
}

@end
