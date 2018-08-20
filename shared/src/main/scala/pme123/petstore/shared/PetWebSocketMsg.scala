package pme123.petstore.shared

import julienrf.json.derived
import play.api.libs.json.OFormat
import pme123.petstore.shared.services.User

sealed trait PetWebSocketMsg {

}

object PetWebSocketMsg {
  implicit val jsonFormat: OFormat[PetWebSocketMsg] = derived.oformat[PetWebSocketMsg]()

}

// as with akka-http the web-socket connection will be closed when idle for too long.
case object KeepAliveMsg extends PetWebSocketMsg


case class PathMsg(username: User.UserId, route: PathMsg.Message, time: Long = 0)
  extends PetWebSocketMsg

object PathMsg {

  type Message = String

}