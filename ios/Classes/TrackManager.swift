import Foundation
import sariska

class TrackManager: NSObject {

    @objc func mute(_ params: NSDictionary) {
       for track in SariskaMediaTransport.getLocalTracks() {
          let track = track as! JitsiLocalTrack
          if (track.getId() == (params["trackId"] as! NSString))  {
              track.mute()
          }
      }
    }

    @objc func unmute(_ params: NSDictionary) {
       for track in SariskaMediaTransport.getLocalTracks() {
          let track = track as! JitsiLocalTrack
          if (track.getId() == (params["trackId"] as! NSString))  {
              track.unmute()
          }
       }
    }

    @objc func switchCamera(_ params: NSDictionary) {
       for track in SariskaMediaTransport.getLocalTracks() {
          let track = track as! JitsiLocalTrack
          if (track.getId() == (params["trackId"] as! NSString))  {
              track.switchCamera()
          }
       }
    }

    @objc func dispose(_ params: NSDictionary) {
       for track in SariskaMediaTransport.getLocalTracks() {
          let track = track as! JitsiLocalTrack
          if (track.getId() == (params["trackId"] as! NSString))  {
              track.dispose()
          }
       }
    }

    @objc func destroy() {

    }
}