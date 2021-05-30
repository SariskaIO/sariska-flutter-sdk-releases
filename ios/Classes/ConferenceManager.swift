import Foundation
import sariska

class ConferenceManager: NSObject, Conference {

    private var emitter: (_ action: String, _ m: [String: Any?]?) -> Void
	let conference: Conference?

    init(_ emitter: @escaping (_ action: String, _ m: [String: Any?]?) -> Void) {
        self.emitter = emitter
    }

    func Release() {
        conference = nil
    }

    func createConference() {
        conference = Conference()
    }

    @objc func join() {
       conference.join()
    }

    @objc func join(_ params: NSDictionary) {
       conference.join(params["password"] as! NSString)
    }

    @objc func grantOwner(_ params: NSDictionary) {
       conference.grantOwner(params["id"] as! NSString)
    }

    @objc func setStartMutedPolicy(_ params: NSDictionary) {
       conference.setStartMutedPolicy(params["policy"] as! NSDictionary)
    }

    @objc func setReceiverVideoConstraint(_ params: NSDictionary) {
       conference.setReceiverVideoConstraint(params["number"] as! NSNumber)
    }

    @objc func setSenderVideoConstraint  (_ params: NSDictionary) {
       conference.setSenderVideoConstraint(params["number"] as! NSNumber)
    }

    @objc func sendMessage(_ params: NSDictionary) {
       if ((params["to"] as! NSString) != "") {
           conference.sendMessage(params["message"] as! NSString, params["to"] as! NSString)
       } else {
           conference.sendMessage(params["message"] as! NSString)
       }
    }

    @objc func setLastN(_ params: NSDictionary) {
       conference.setLastN(params["num"] as! NSNumber)
    }

    @objc func dial(_ params: NSDictionary) {
       conference.dial(params["number"] as! NSNumber)
    }

    @objc func muteParticipant(_ params: NSDictionary) {
       if (params["mediaType"] as! NSString) {
           conference.muteParticipant(params["id"] as! NSString, params["mediaType"] as! NSString)
       } else {
           conference.muteParticipant(params["id"] as! NSNumber)
       }
    }

    @objc func setDisplayName(_ params: NSDictionary) {
       conference.setDisplayName(params["displayName"] as! NSString)
    }

    @objc func addTrack(_ params: NSDictionary) {
        for track in conference.getLocalTracks() {
           let track = track as! JitsiLocalTrack
           if (track.getId() == (params["trackId"] as! NSString))  {
               conference.addTrack(track)
           }
       }
    }

    @objc func removeTrack(_ params: NSDictionary) {
        for track in conference.getLocalTracks() {
           let track = track as! JitsiLocalTrack
           if (track.getId() == (params["trackId"] as! NSString))  {
               conference.removeTrack(track)
           }
       }
    }

    @objc func replaceTrack(_ params: NSDictionary) {
        let oldTrack: JitsiLocalTrack?
        let newTrack: JitsiLocalTrack?
        for track in conference.getLocalTracks() {
           let track = track as! JitsiLocalTrack
           if (track.getId() == (params["oldTrackId"] as! NSString))  {
               oldTrack = track
           }
           if (track.getId() == (params["newTrackId"] as! NSString))  {
              newTrack = track
           }
       }
       conference.replaceTrack(oldTrack, newTrack)
    }

    @objc func lock(_ params: NSDictionary) {
       conference.lock(params["password"] as! NSString)
    }

    @objc func setSubject(_ params: NSDictionary) {
       conference.lock(params["subject"] as! NSString)
    }

    @objc func unlock() {
       conference.unlock()
    }

    @objc func kickParticipant(_ params: NSDictionary) {
       conference.kickParticipant(params["id"] as! NSString)
    }

    @objc func pinParticipant(_ params: NSDictionary) {
       conference.pinParticipant(params["id"] as! NSString)
    }

    @objc func selectParticipant(_ params: NSDictionary) {
       conference.selectParticipant(params["id"] as! NSString)
    }

    @objc func selectParticipants(_ params: NSDictionary) {
       conference.selectParticipants(params["participantIds"] as! NSArray)
    }

    @objc func startTranscriber() {
       conference.startTranscriber()
    }

    @objc func stopTranscriber() {
       conference.stopTranscriber()
    }

    @objc revokeOwner func (_ params: NSDictionary) {
       conference.revokeOwner(params["id"] as! NSString)
    }

    @objc func startRecording(_ params: NSDictionary) {
       conference.startRecording(params["options"] as! NSDictionary)
    }

    @objc func stopRecording(_ params: NSDictionary) {
       conference.stopRecording(params["sessionID"] as! NSString)
    }

    @objc func setLocalParticipantProperty(_ params: NSDictionary) {
       conference.setLocalParticipantProperty(params["propertyKey"] as! NSString, params["propertyValue"] as! NSString)
    }

    @objc func removeLocalParticipantProperty(_ params: NSDictionary) {
       conference.removeLocalParticipantProperty(params["name"] as! NSString)
    }

    @objc func sendFeedback(_ params: NSDictionary) {
       conference.sendFeedback(params["overallFeedback"] as! NSString, params["detailedFeedback"] as! NSString)
    }

    @objc func leave() {
       conference.leave()
    }

    @objc func destroy() {
        self?.Release()
    }

    @objc func newConferenceMessage(_ action: NSString, _ m: NSDictionary) {
       emitter(action, m)
    }
}