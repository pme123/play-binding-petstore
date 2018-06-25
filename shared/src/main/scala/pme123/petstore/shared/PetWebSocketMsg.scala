package pme123.petstore.shared

import julienrf.json.derived
import play.api.libs.json.OFormat

sealed trait PetWebSocketMsg {

}
object PetWebSocketMsg {
  implicit val jsonFormat: OFormat[PetWebSocketMsg] = derived.oformat[PetWebSocketMsg]()

}

// as with akka-http the web-socket connection will be closed when idle for too long.
case object KeepAliveMsg extends PetWebSocketMsg
