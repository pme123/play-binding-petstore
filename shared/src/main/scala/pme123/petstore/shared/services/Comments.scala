package pme123.petstore.shared.services

import java.time.Instant

import julienrf.json.derived
import play.api.libs.json.OFormat

case class Comments(user: User, comments: Seq[Comment] = Nil) {
}

object Comments {
  implicit val jsonFormat: OFormat[Comments] = derived.oformat[Comments]()

}

case class Comment(user: User, text: String, created: Instant = Instant.now(), replies: Seq[Comment] = Nil)

object Comment extends InstantHelper {
  implicit val jsonFormat: OFormat[Comment] = derived.oformat[Comment]()

}