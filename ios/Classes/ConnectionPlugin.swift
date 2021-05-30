import Foundation
import sariska

public class ConnectionPlugin: NSObject, FlutterPlugin, FlutterStreamHandler {
    private final weak var ConnectionPlugin: ConnectionPlugin?
    private var methodChannel: FlutterMethodChannel?
    private var eventChannel: FlutterEventChannel?
    private var eventSink: FlutterEventSink? = nil

    private lazy var manager: ConnectionManager = {
        return ConnectionManager() { [weak self] action, m in
            self?.emit(action, m)
        }
    }()

    init(_ connectionPlugin: ConnectionPlugin) {
        self.connectionPlugin = connectionPlugin
    }

    public static func register(with registrar: FlutterPluginRegistrar) {

    }

    public func initPlugin(_ registrar: FlutterPluginRegistrar) {
        methodChannel = FlutterMethodChannel(name: "connection", binaryMessenger: registrar.messenger())
        eventChannel = FlutterEventChannel(name: "connection/events", binaryMessenger: registrar.messenger())
        registrar.addMethodCallDelegate(self, channel: methodChannel!)
        eventChannel?.setStreamHandler(self)
    }

    public func detachFromEngine(for registrar: FlutterPluginRegistrar) {
        methodChannel?.setMethodCallHandler(nil)
        eventChannel?.setStreamHandler(nil)
        manager.Release()
    }

    public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        eventSink = events
        return nil
    }

    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        eventSink = nil
        return nil
    }

    private func emit(_ action: String, _ m: Dictionary<String, Any?>?) {
        var event: Dictionary<String, Any?> = ["action": action]
        if let `m` = m {
            event.merge(m) { (current, _) in
                current
            }
        }
        eventSink?(event)
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