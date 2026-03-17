//
//  UISegmentedControl+AGi18n.m
//  Pods
//
//  Created by Angel Garcia on 28/01/14.
//
//

#import "UISegmentedControl+AGi18n.h"
#import "MBTBiziz-Swift.h"

@implementation UISegmentedControl (AGi18n)

- (void)localizeFromNib {
    //Replace text with localizable version
    for (NSInteger segment = 0; segment < self.numberOfSegments; segment++) {
        NSString *title = [self titleForSegmentAtIndex:segment];
        if(title.length>0)
        {
            [self setTitle:[[MBTLanguageManager sharedManager] getLocalizedStringWithKey:title] forSegmentAtIndex:segment];
        }
    }
}

@end
