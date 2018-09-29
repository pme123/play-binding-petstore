package pme123.petstore.shared.services

import java.time.Instant

import julienrf.json.derived
import play.api.libs.json.OFormat

case class Conversations(conversations: Seq[Conversation] = Nil) {
}

object Conversations {
  implicit val jsonFormat: OFormat[Conversations] = derived.oformat[Conversations]()

}

case class Conversation(id: Long, username: String, comments: Seq[Comment], active: Boolean = true)

object Conversation extends InstantHelper {
  implicit val jsonFormat: OFormat[Conversation] = derived.oformat[Conversation]()

}

case class Comment(user: User, text: String, created: Instant = Instant.now())

object Comment extends InstantHelper {
  implicit val jsonFormat: OFormat[Comment] = derived.oformat[Comment]()

}

case class NewComment(username: String, text: String, conversation: Long)

object NewComment extends InstantHelper {
  implicit val jsonFormat: OFormat[NewComment] = derived.oformat[NewComment]()

}

