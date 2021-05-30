import Foundation
import sariska

public class SwiftSariskaMediaTransportPlugin: NSObject, FlutterPlugin, FlutterStreamHandler, SariskaMediaTransport {
    private var methodChannel: FlutterMethodChannel?
    private var eventChannel: FlutterEventChannel?
    private var eventSink: FlutterEventSink? = nil

    init(_ sariskaMediaTransport: SwiftSariskaMediaTransportPlugin) {
        self.sariskaMediaTransport = sariskaMediaTransport
    }

    private lazy var connectionPlugin: ConnectionPlugin = {
        return ConnectionPlugin(self)
    }()

    private lazy var conferencePlugin: ConferencePlugin = {
        return conferencePlugin(self)
    }()

    private lazy var trackPlugin: trackPlugin = {
        return TrackPlugin(self)
    }()

    public static func register(with registrar: FlutterPluginRegistrar) {
        let sariskaMediaTransport = SwiftSariskaMediaTransportPlugin()
        sariskaMediaTransport.registrar = registrar
        sariskaMediaTransport.conferencePlugin.initPlugin(registrar)
        sariskaMediaTransport.connectionPlugin.initPlugin(registrar)
        sariskaMediaTransport.trackPlugin.initPlugin(registrar)
        sariskaMediaTransport.initPlugin(registrar)
    }

    public func initPlugin(_ registrar: FlutterPluginRegistrar) {
        methodChannel = FlutterMethodChannel(name: "sariska_media_transport", binaryMessenger: registrar.messenger())
        eventChannel = FlutterEventChannel(name: "sariska_media_transport/events", binaryMessenger: registrar.messenger())
        registrar.addMethodCallDelegate(self, channel: methodChannel!)
        eventChannel?.setStreamHandler(self)
        registrar.register(SariskaSurfaceViewFactory(registrar.messenger()), withId: "SariskaSurfaceView")
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

    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        eventSink = nil
        return nil
    }

    private func emit(_ methodName: String, _ data: Dictionary<String, Any?>?) {
        var event: Dictionary<String, Any?> = ["methodName": methodName]
        if let `data` = data {
            event.merge(data) { (current, _) in
                current
            }
        }
        eventSink?(event)
    }

    @objc newSariskaMediaTransportMessage(action: String, m: NSDictionary) {
    }

    @objc newLocalTrackMessage(action: String, m: NSArray) {
       emit(action, m)
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        if (call.method == "initializeSdk") {
            self.initializeSdk()
            return
        }
        if (call.method == "createLocalTracks") {
            self.createLocalTracks(options)
            return
        }
        result(FlutterMethodNotImplemented)
    }
}