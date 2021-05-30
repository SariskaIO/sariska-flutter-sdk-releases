import Foundation
import sariska

public class TrackPlugin: NSObject, FlutterPlugin, FlutterStreamHandler {
    private final weak var trackPlugin: TrackPlugin?
    private var methodChannel: FlutterMethodChannel?

    private lazy var manager: TrackManger = {
        return TrackManger()
    }()

    init(_ trackPlugin: TrackPlugin) {
        self.trackPlugin = trackPlugin
    }

    public static func register(with registrar: FlutterPluginRegistrar) {

    }

    public func initPlugin(_ registrar: FlutterPluginRegistrar) {
        methodChannel = FlutterMethodChannel(name: "track", binaryMessenger: registrar.messenger())
        registrar.addMethodCallDelegate(self, channel: methodChannel!)
    }

    public func detachFromEngine(for registrar: FlutterPluginRegistrar) {
        methodChannel?.setMethodCallHandler(nil)
    }


    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if let params = call.arguments as? NSDictionary {
            let selector = NSSelectorFromString(call.method + "::")
            if manager.responds(to: selector) {
                manager.perform(selector, with: params)
                return
            }
        } else {
            let selector = NSSelectorFromString(call.method + ":")
            if manager.responds(to: selector) {
                manager.perform(selector)
                return
            }
        }
        result(FlutterMethodNotImplemented)
    }
}