//
//  UIButton+AGi18n.m
//  AGi18n
//
//  Created by Angel Garcia on 3/13/13.
//  Copyright (c) 2013 angelolloqui.com. All rights reserved.
//

#import "UIButton+AGi18n.h"
#import "MBTBiziz-Swift.h"

@implementation UIButton (AGi18n)

- (void)localizeFromNib {

    //Replace text with localizable version
    NSArray *states = @[@(UIControlStateNormal), @(UIControlStateHighlighted), @(UIControlStateDisabled), @(UIControlStateSelected), @(UIControlStateApplication)];
    for (NSNumber *state in states) {
        NSString *title = [self titleForState:state.integerValue];
        if (title.length > 0) {
            [UIView performWithoutAnimation:^{
                [self setTitle:[[MBTLanguageManager sharedManager] getLocalizedStringWithKey:title] forState:state.integerValue];
                [self layoutIfNeeded];
            }];
        }
    }
}

@end
