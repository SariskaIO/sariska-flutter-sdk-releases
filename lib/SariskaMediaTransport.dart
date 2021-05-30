import 'dart:async';
import 'package:flutter/services.dart';
import 'package:sariska/JitsiLocalTrack.dart';
import 'Connection.dart';

typedef void LocalTrackCallback(List<JitsiLocalTrack> tracks);

class SariskaMediaTransport {
  static const MethodChannel _methodChannel =
      MethodChannel('sariska_media_transport');
  static const EventChannel _eventChannel =
      EventChannel('sariska_media_transport/events');
  static late LocalTrackCallback localTrackCallback;
  static List<JitsiLocalTrack> localTracks = [];

  SariskaMediaTransport._() {
    _eventChannel.receiveBroadcastStream().listen((event) {
      final eventMap = Map<String, dynamic>.from(event);
      final action = eventMap['action'] as String;
      final m = List<dynamic>.from(eventMap['m']);
      switch (action) {
        case "CREATE_LOCAL_TRACKS":
          localTracks = [];
          for (Map<String, dynamic> track in m) {
            localTracks.add(new JitsiLocalTrack(track));
          }
          localTrackCallback(localTracks);
          break;
        default:
      }
    });
  }

  static Future<T?> _invokeMethod<T>(String method,
      [Map<String, dynamic>? arguments]) {
    return _methodChannel.invokeMethod(method, arguments);
  }

  static initializeSdk() {
    _invokeMethod('initializeSdk');
  }

  static List<JitsiLocalTrack> getLocalTracks() {
    return localTracks;
  }

  static Connection JitsiConnection(String token) {
    return new Connection(token);
  }

  static void createLocalTracks(
      Map<String, dynamic> options, LocalTrackCallback callback) {
    localTrackCallback = callback;
    _invokeMethod('createLocalTracks', {'options': options});
  }
}
