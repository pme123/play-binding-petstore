package pme123.petstore.server.entity

import java.nio.charset.StandardCharsets
import java.util.Base64

import com.mohiva.play.silhouette.api
import julienrf.json.derived
import play.api.libs.json.{Format, Json, OFormat}
import pme123.petstore.shared.services.User

case class Identity(username: String
                    , password: String) {

  def encodeAuthorization(): String =
    Base64.getEncoder
      .encodeToString(s"$username:$password"
        .getBytes(StandardCharsets.UTF_8))
}

object Identity {
  implicit val jsonFormat: Format[Identity] = Json.format[Identity]

}

case class AuthUser(id: User.UserId,
                    groups: Set[User.GroupId] = Set()
                   ) extends api.Identity {

  val isAdmin: Boolean = groups.contains(User.adminGroup)

}

object AuthUser {

  implicit val jsonFormat: OFormat[AuthUser] = derived.oformat[AuthUser]()

}

