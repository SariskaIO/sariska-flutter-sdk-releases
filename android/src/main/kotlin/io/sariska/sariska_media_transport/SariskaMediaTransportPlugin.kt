package io.sariska.sariska_media_transport

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.*
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.plugin.platform.PlatformViewRegistry
import io.sariska.sdk.SariskaMediaTransport


class SariskaMediaTransportPlugin : FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler,
    ActivityAware, SariskaMediaTransport() {
    private var registrar: Registrar? = null
    private var binding: FlutterPlugin.FlutterPluginBinding? = null
    private lateinit var applicationContext: Context
    private lateinit var application: Application
    private lateinit var methodChannel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private var eventSink: EventChannel.EventSink? = null
    private val handler = Handler(Looper.getMainLooper())
    private val connectionPlugin = ConnectionPlugin()
    private val conferencePlugin = ConferencePlugin()
    private val trackPlugin = TrackPlugin()

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            SariskaMediaTransportPlugin().apply {
                this.registrar = registrar
                connectionPlugin.initPlugin(registrar.messenger())
                conferencePlugin.initPlugin(registrar.messenger())
                trackPlugin.initPlugin(registrar.messenger())
                initPlugin(registrar.context(), registrar.messenger(), registrar.platformViewRegistry())
            }
        }
    }

    private fun initPlugin(context: Context, binaryMessenger: BinaryMessenger, platformViewRegistry: PlatformViewRegistry) {
        applicationContext = context.applicationContext
        methodChannel = MethodChannel(binaryMessenger, "sariska_media_transport")
        methodChannel.setMethodCallHandler(this)
        eventChannel = EventChannel(binaryMessenger, "sariska_media_transport/events")
        eventChannel.setStreamHandler(this)
        platformViewRegistry.registerViewFactory("SariskaSurfaceView", SariskaSurfaceViewFactory(binaryMessenger))
    }

    override fun onAttachedToEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        this.binding = binding
        connectionPlugin.onAttachedToEngine(binding)
        conferencePlugin.onAttachedToEngine(binding)
        trackPlugin.onAttachedToEngine(binding)
        initPlugin(binding.applicationContext, binding.binaryMessenger, binding.platformViewRegistry)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        connectionPlugin.onDetachedFromEngine(binding)
        conferencePlugin.onDetachedFromEngine(binding)
        trackPlugin.onDetachedFromEngine(binding)
        methodChannel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }

    private fun emit(action: String, m: MutableList<Map<String, Any?>?>) {
        handler.post {
            val event: MutableMap<String, Any?> = mutableMapOf("action" to action)
            event["m"] = m
            eventSink?.success(event)
        }
    }

    override fun newLocalTrackMessage(action: String?, tracks: ReadableArray) {
        val list: MutableList<Map<String, Any?>?> = mutableListOf<Map<String, Any?>?>()
        for (i in 0 until tracks.size()) {
            val track = tracks.getMap(i)
            list.add(Utils.toMap(track))
        }
        if (action != null) {
            emit(action, list)
        }
    }

    override fun newSariskaMediaTransportMessage(action: String?, readableMap: ReadableMap?) {

    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "initializeSdk") {
            initializeSdk(application)
            return
        }
        if (call.method == "createLocalTracks") {
            call.arguments<Map<*, *>>()?.toMutableMap()?.let {
                createLocalTracks(it["options"] as Bundle?, null)
            }
            return
        }
        result.notImplemented()
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        application = binding.getActivity().getApplication()
    }

    override fun onDetachedFromActivityForConfigChanges() {
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    }

    override fun onDetachedFromActivity() {
    }
}
