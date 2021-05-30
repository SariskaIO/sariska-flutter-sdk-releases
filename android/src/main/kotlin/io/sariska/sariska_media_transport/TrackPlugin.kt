package io.sariska.sariska_media_transport

import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

class TrackPlugin: FlutterPlugin, MethodCallHandler {
    private lateinit var methodChannel: MethodChannel
    private val manager = TrackManager()

    fun initPlugin(binaryMessenger: BinaryMessenger) {
        methodChannel = MethodChannel(binaryMessenger, "track")
        methodChannel.setMethodCallHandler(this)
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        initPlugin(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        methodChannel.setMethodCallHandler(null)
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
