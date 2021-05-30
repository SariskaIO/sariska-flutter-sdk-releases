package io.sariska.sariska_media_transport

import android.os.Bundle
import com.facebook.react.bridge.ReadableMap
import io.sariska.sdk.Conference
import io.sariska.sdk.JitsiLocalTrack

class ConferenceManager(
        private val emit: (action: String, m: Map<String, Any?>?) -> Unit
) : Conference() {

    lateinit var conference: Conference

    fun createConference() {
        conference = Conference()
    }

    override fun join() {
        conference.join()
    }

    fun join(params: Map<String, *>) {
        conference.join(params["password"] as? String)
    }

    fun grantOwner(params: Map<String, *>) {
        conference.grantOwner(params["id"] as? String)
    }

    fun setStartMutedPolicy(params: Map<String, *>) {
        conference.setStartMutedPolicy(params["policy"] as? Bundle)
    }

    fun setReceiverVideoConstraint(params: Map<String, *>) {
        conference.setReceiverVideoConstraint(params["number"] as Int)
    }

    fun setSenderVideoConstraint(params: Map<String, *>) {
        conference.setSenderVideoConstraint(params["number"] as Int)
    }

    fun sendMessage(params: Map<String, *>) {
        if ((params["to"] as? String) != "") {
            conference.sendMessage(params["message"] as? String, params["to"] as? String)
        } else {
            conference.sendMessage(params["message"] as? String)
        }
    }

    fun setLastN(params: Map<String, *>) {
        conference.setLastN(params["num"] as Int)
    }

    fun dial(params: Map<String, *>) {
        conference.dial(params["number"] as Int)
    }

    fun muteParticipant(params: Map<String, *>) {
        if (params["mediaType"] as? String != "") {
            conference.muteParticipant(params["id"] as? String, params["mediaType"] as? String)
        } else {
            conference.muteParticipant(params["id"] as? String)
        }
    }

    fun setDisplayName(params: Map<String, *>) {
        conference.setDisplayName(params["displayName"] as? String)
    }

    fun addTrack(params: Map<String, *>) {
        for (track in conference.getLocalTracks()) {
            if (track.getId() == (params["trackId"] as? String)) {
                conference.addTrack(track)
            }
        }
    }

    fun removeTrack(params: Map<String, *>) {
        for (track in conference.getLocalTracks()) {
            if (track.getId() == (params["trackId"] as? String)) {
                conference.removeTrack(track)
            }
        }
    }

    fun replaceTrack(params: Map<String, *>) {
        var oldTrack : JitsiLocalTrack? = null
        var newTrack : JitsiLocalTrack? = null
        for (track in conference.getLocalTracks()) {
            if (track.getId() == (params["oldTrackId"] as? String)) {
                oldTrack = track
            }
            if (track.getId() == (params["newTrackId"] as? String)) {
                newTrack = track
            }
        }
        conference.replaceTrack(oldTrack, newTrack)
    }

    fun lock(params: Map<String, *>) {
        conference.lock(params["password"] as? String)
    }

    fun setSubject(params: Map<String, *>) {
        conference.lock(params["subject"] as? String)
    }

    override fun unlock() {
        conference.unlock()
    }

    fun kickParticipant(params: Map<String, *>) {
        conference.kickParticipant(params["id"] as? String)
    }

    fun pinParticipant(params: Map<String, *>) {
        conference.pinParticipant(params["id"] as? String)
    }

    fun selectParticipant(params: Map<String, *>) {
        conference.selectParticipant(params["id"] as? String)
    }

    fun selectParticipants(params: Map<String, *>) {
        conference.selectParticipants(params["participantIds"] as? Array<String>)
    }

    override fun startTranscriber() {
        conference.startTranscriber()
    }

    override fun stopTranscriber() {
        conference.stopTranscriber()
    }

    fun revokeOwner(params: Map<String, *>) {
        conference.revokeOwner(params["id"] as? String)
    }

    fun startRecording(params: Map<String, *>) {
        conference.startRecording(params["options"] as? Bundle)
    }

    fun stopRecording(params: Map<String, *>) {
        conference.stopRecording(params["sessionID"] as? String)
    }

    fun setLocalParticipantProperty(params: Map<String, *>) {
        conference.setLocalParticipantProperty(params["propertyKey"] as? String, params["propertyValue"] as? String)
    }

    fun removeLocalParticipantProperty(params: Map<String, *>) {
        conference.removeLocalParticipantProperty(params["name"] as? String)
    }

    fun sendFeedback(params: Map<String, *>) {
        conference.sendFeedback(params["overallFeedback"] as? String, params["detailedFeedback"] as? String)
    }

    override fun leave() {
        conference.leave()
    }

    fun destroy() {
        conference.leave()
    }

    fun release() {
       conference.leave()
    }

    override fun newConferenceMessage(action: String?, m: ReadableMap?) {
        if (action != null) {
            emit(action, m?.let { Utils.toMap(it) })
        }
    }
}
