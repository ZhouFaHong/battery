#import "AppDelegate.h"
#import "GeneratedPluginRegistrant.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    // 获取window
    NSArray *windows = [UIApplication sharedApplication].windows;
    UIWindow *window = windows.firstObject;
    // 获取FlutterViewController
    FlutterViewController *controller = (FlutterViewController*) window.rootViewController;
    // 创建 FlutterMethodChannel 对应 dart 调用的方法
    FlutterMethodChannel *channel = [FlutterMethodChannel methodChannelWithName:@"battery" binaryMessenger:controller];
    
    // 监听调用回调
    [channel setMethodCallHandler:^(FlutterMethodCall * _Nonnull call, FlutterResult  _Nonnull result) {
        // 调用 getBatteryLevel方法
        if ([call.method  isEqual: @"getBatteryLevel"]) {
            [self getBatteryLevel:result];
        }
    }];
    
    [GeneratedPluginRegistrant registerWithRegistry:self];
    // Override point for customization after application launch.
    return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

// 获取电量
- (void)getBatteryLevel:(FlutterResult)result{
    
    UIDevice *device = [UIDevice currentDevice];
    device.batteryMonitoringEnabled = YES;
    if (device.batteryState == UIDeviceBatteryStateUnknown) {
        result([FlutterError errorWithCode:@"Unavailable" message:@"Battery Level is not available"
        details:nil]);
    }else{
        double deviceLevel = [UIDevice currentDevice].batteryLevel;
        result(@((int)(deviceLevel * 100)));
    }
}

@end
