package pme123.petstore.shared.services

import julienrf.json.derived
import play.api.libs.json.OFormat

case class User(username: User.UserId,
                groups: Set[User.GroupId],
                firstName: String,
                lastName: String,
                email: String,
                avatar: String,
                language: Language) {

  lazy val fullName = s"$firstName $lastName"

  val groupsString: String = groups.mkString(",")
  val languageString: String = language.entryName

  val isAdmin: Boolean = groups.contains(User.adminGroup)
  val isManager: Boolean = groups.contains(User.managerGroup)
  val isCustomer: Boolean = groups.contains(User.customerGroup)

}

object User {
  type UserId = String
  type GroupId = String

  val adminGroup = "admin"
  val managerGroup = "manager"
  val customerGroup = "customer"

  def apply(username: UserId,
            groups: String,
            firstName: String,
            lastName: String,
            email: String,
            avatar: String,
            language: String): User = new User(username, groups.splitToSet, firstName, lastName, email, avatar, Language.withNameInsensitive(language))

  implicit val jsonFormat: OFormat[User] = derived.oformat[User]()

}

case class LoggedInUser(maybeUser: Option[User] = None) {

  val isDefined: Boolean = maybeUser.isDefined

  val isAdmin: Boolean = maybeUser.isDefined && maybeUser.get.isAdmin
  val isManager: Boolean = maybeUser.isDefined && maybeUser.get.isManager
  val isCustomer: Boolean = maybeUser.isEmpty || maybeUser.get.isCustomer

  val avatar: String = {
    val avatar = maybeUser.map(_.avatar).getOrElse("anonymous.png")
    s"/images/users/$avatar"
  }
  val fullName: String = maybeUser.map(_.fullName).getOrElse("Anonymous")

  val username: String = maybeUser.map(_.username).getOrElse("anonymous")
}

object LoggedInUser {
  implicit val jsonFormat: OFormat[LoggedInUser] = derived.oformat[LoggedInUser]()

}


