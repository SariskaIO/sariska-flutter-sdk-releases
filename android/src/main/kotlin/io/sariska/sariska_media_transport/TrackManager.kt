package io.sariska.sariska_media_transport
import io.sariska.sdk.SariskaMediaTransport


class TrackManager {
    fun mute(params: Map<String, *>) {
        for(track in SariskaMediaTransport.getLocalTracks()) {
            if (track.getId() == (params["trackId"] as? String))  {
                track.mute()
            }
        }
    }

    fun unmute(params: Map<String, *>) {
        for(track in SariskaMediaTransport.getLocalTracks()) {
            if (track.getId() == (params["trackId"] as? String))  {
                track.unmute()
            }
        }
    }

    fun switchCamera(params: Map<String, *>) {
        for(track in SariskaMediaTransport.getLocalTracks()) {
            if (track.getId() == (params["trackId"] as? String))  {
                track.switchCamera()
            }
        }
    }

    fun dispose(params: Map<String, *>) {
        for(track in SariskaMediaTransport.getLocalTracks()) {
            if (track.getId() == (params["trackId"] as? String))  {
                track.dispose()
            }
        }
    }

    fun destroy() {
    }
}
