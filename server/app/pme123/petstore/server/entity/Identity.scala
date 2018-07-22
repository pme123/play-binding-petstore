package pme123.petstore.server.entity

import java.nio.charset.StandardCharsets
import java.util.Base64

import play.api.libs.json.{Format, Json}

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


