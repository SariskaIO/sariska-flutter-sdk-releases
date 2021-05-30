package io.sariska.sariska_media_transport

import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.ReadableType
import java.util.HashMap

class Utils {
    companion object {
        fun toMap(readableMap: ReadableMap): Map<String, Any?>? {
            val map: MutableMap<String, Any?> = HashMap()
            val iterator = readableMap.keySetIterator()
            while (iterator.hasNextKey()) {
                val key: String = iterator.nextKey()
                val type = readableMap.getType(key)
                when (type) {
                    ReadableType.Null -> map[key] = null
                    ReadableType.Boolean -> map[key] = readableMap.getBoolean(key)
                    ReadableType.Number -> map[key] = readableMap.getDouble(key)
                    ReadableType.String -> map[key] = readableMap.getString(key)
                }
            }
            return map
        }
    }
}