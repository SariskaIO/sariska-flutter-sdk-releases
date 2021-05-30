package io.sariska.sariska_media_transport

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class ConnectionPlugin: FlutterPlugin, MethodCallHandler, EventChannel.StreamHandler {
    private lateinit var methodChannel: MethodChannel
    private lateinit var eventChannel: EventChannel
    private var eventSink: EventChannel.EventSink? = null
    private val manager = ConnectionManager { action -> emit(action) }
    private val handler = Handler(Looper.getMainLooper())

    fun initPlugin(binaryMessenger: BinaryMessenger) {
        methodChannel = MethodChannel(binaryMessenger, "connection")
        methodChannel.setMethodCallHandler(this)
        eventChannel = EventChannel(binaryMessenger, "connection/events")
        eventChannel.setStreamHandler(this)
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        initPlugin(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
        eventChannel.setStreamHandler(null)
        manager.release()
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }

    private fun emit(action: String) {
        handler.post {
            val event: MutableMap<String, Any?> = mutableMapOf("action" to action)
            eventSink?.success(event)
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        manager.javaClass.declaredMethods.find { it.name == call.method }?.let { function ->
            function.let { method ->
                try {
                    val parameters = mutableListOf<Any?>()
                    call.arguments<Map<*, *>>()?.toMutableMap()?.let {
                        parameters.add(it)
                    }
                    method.invoke(manager, *parameters.toTypedArray())
                    return@onMethodCall
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        result.notImplemented()
    }
}
