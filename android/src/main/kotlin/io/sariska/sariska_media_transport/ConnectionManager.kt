package io.sariska.sariska_media_transport
import io.sariska.sdk.Connection

class ConnectionManager(
        private val emit: (action: String) -> Unit
) : Connection("") {
    lateinit var connection: Connection

    fun createConnection(params: Map<String, *>) {
        connection = Connection(params["token"] as? String)
    }

    override fun newConnectionMessage(action: String?) {
        if (action != null) {
            emit(action)
        }
    }

    fun destroy() {
        connection.disconnect()
    }

    override fun connect() {
        connection.connect()
    }

    override fun disconnect() {
        connection.disconnect()
    }

    override fun addFeature() {
        connection.addFeature()
    }

    override fun removeFeature() {
        connection.removeFeature()
    }

    fun release(){
        connection.disconnect();
    }
}
