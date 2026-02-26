//
//  UIBarButtonItem+AGi18n.m
//  AGi18n
//
//  Created by Angel Garcia on 3/17/13.
//  Copyright (c) 2013 angelolloqui.com. All rights reserved.
//

#import "UIBarButtonItem+AGi18n.h"
#import "MBTBiziz-Swift.h"

@implementation UIBarButtonItem (AGi18n)

- (void)localizeFromNib {
    //Replace text with localizable version
    if (self.title.length > 0) {
        self.title = [[MBTLanguageManager sharedManager] getLocalizedStringWithKey:self.title];
    }
}

@end
