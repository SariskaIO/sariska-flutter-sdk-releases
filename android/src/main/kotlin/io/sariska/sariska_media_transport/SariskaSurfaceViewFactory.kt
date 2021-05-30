package io.sariska.sariska_media_transport

import android.content.Context
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.oney.WebRTCModule.WebRTCView
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import io.sariska.sdk.SariskaMediaTransport

class SariskaSurfaceViewFactory(
    private val messenger: BinaryMessenger
) : PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        return SariskaSurfaceView(context.applicationContext, messenger, viewId, args as? Map<*, *>)
    }
}

class SariskaSurfaceView(
    context: Context,
    messenger: BinaryMessenger,
    viewId: Int,
    args: Map<*, *>?
) : PlatformView, MethodChannel.MethodCallHandler {
    private val view = WebRTCView(SariskaMediaTransport.getReactContext())
    private val channel = MethodChannel(messenger, "sariska/surface_view_$viewId")

    init {
        args?.let { map ->
            (map["streamURL"] as? String)?.let { setStreamURL(it) }
            (map["objectFit"] as? String)?.let { setObjectFit(it) }
            (map["mirror"] as? Boolean)?.let { setMirror(it) }
            (map["zOrder"] as? Int)?.let { setZOrder(it) }
        }
        channel.setMethodCallHandler(this)
    }

    override fun getView(): View {
        return view
    }

    override fun dispose() {
        channel.setMethodCallHandler(null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        this.javaClass.declaredMethods.find { it.name == call.method }?.let { function ->
            function.let { method ->
                val parameters = mutableListOf<Any?>()
                function.parameters.forEach { parameter ->
                    val map = call.arguments<Map<*, *>>()
                    if (map.containsKey(parameter.name)) {
                        parameters.add(map[parameter.name])
                    }
                }
                try {
                    method.invoke(this, *parameters.toTypedArray())
                    return@onMethodCall
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        result.notImplemented()
    }

    private fun setStreamURL(streamURL: String) {
        view.setStreamURL(streamURL)
    }

    private fun setObjectFit(objectFit: String) {
        view.setObjectFit(objectFit)
    }

    private fun setMirror(mirror: Boolean) {
        view.setMirror(mirror)
    }

    private fun setZOrder(onTop: Int) {
        view.setZOrder(onTop)
    }
}