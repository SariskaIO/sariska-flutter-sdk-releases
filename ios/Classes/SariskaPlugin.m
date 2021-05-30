#import "SariskaPlugin.h"
#if __has_include(<sariska/sariska-Swift.h>)
#import <sariska/sariska-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "sariska-Swift.h"
#endif

@implementation SariskaPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftSariskaPlugin registerWithRegistrar:registrar];
}
@end
