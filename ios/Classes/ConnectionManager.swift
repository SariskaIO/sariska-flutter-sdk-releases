import Foundation
import sariska

class ConnectionManager: NSObject, Connection {
    private var emitter: (_ action: String, _ m: [String: Any?]?) -> Void
	  let connection: Connection?

    init(_ emitter: @escaping (_ action: String, _ m: [String: Any?]?) -> Void) {
        self.emitter = emitter
    }

    func Release() {
        connection = nil
    }

    @objc func createConnection(_ params: NSDictionary) {
        connection = Connection(params["token"] as! NSString)
    }

    @objc func connect() {
        connection.connect()
    }

    @objc func disconnect() {
        connection.disconnect()
    }

    @objc func addFeature() {
        connection.addFeature()
    }

    @objc func removeFeature() {
        connection.removeFeature()
    }

}