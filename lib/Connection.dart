import 'dart:async';
import 'package:flutter/services.dart';
import 'Conference.dart';

class ConnectionBinding<T> {
  String event;
  T callback;

  ConnectionBinding(this.event, T this.callback) {
    this.event = event;
    this.callback = callback;
  }

  String getEvent() {
    return this.event;
  }

  T getCallback() {
    return this.callback;
  }
}


 class Connection {
  static List<ConnectionBinding> _bindings = [];
  static const MethodChannel _methodChannel = MethodChannel('conference');
  static const EventChannel _eventChannel = EventChannel('conference/events');

  Connection(String token) {
    _invokeMethod('createConnection', {'token': token});
  }

  Connection._() {
    _eventChannel.receiveBroadcastStream().listen((event) {
      final eventMap = Map<String, dynamic>.from(event);
      final action = eventMap['action'] as String;
      for (var i = 0; i < _bindings.length; i++) {
        if (_bindings[i].getEvent() == action) {
          switch (action) {
            case "CONNECTION_ESTABLISHED":
            case "CONNECTION_FAILED":
            case "CONNECTION_DISCONNECTED":
              _bindings[i].getCallback()();
          }
        }
      }
    });
  }

  static Future<T?> _invokeMethod<T>(String method, [Map<String, dynamic>? arguments]) {
    return _methodChannel.invokeMethod(method, arguments);
  }

  Conference initJitsiConference() {
    return new Conference();
  }

   void connect() {
     _invokeMethod('connect');
  }

   void disconnect() {
     _invokeMethod('disconnect');
  }

   void addFeature() {
     _invokeMethod('addFeature');
  }

   void removeFeature() {
     _invokeMethod('removeFeature');
  }

  void addEventListener(String event, dynamic callback) {
    _bindings.add(new ConnectionBinding(event, callback));
  }

  void removeEventListener(event) {
    _bindings = _bindings.where((binding) => binding.event != event).toList();
  }
}