package pme123.petstore.shared.services

import julienrf.json.derived
import play.api.libs.json.OFormat

case class PathMsg(username: PathMsg.Username, msg: PathMsg.Message) {

}

object PathMsg {

  type Username = String
  type Message = String

  implicit val jsonFormat: OFormat[PathMsg] = derived.oformat[PathMsg]()


}