package pme123.petstore.shared.services

import julienrf.json.derived
import play.api.libs.json.OFormat
import pme123.petstore.shared.services.Language.DE

case class User(authUser: AuthUser,
                firstName: String,
                lastName: String,
                email: String,
                avatar: String,
                language: Language = DE) {
  lazy val fullName = s"$firstName $lastName"
}

object User {
  implicit val jsonFormat: OFormat[User] = derived.oformat[User]()

}

case class AuthUser(id: AuthUser.UserId,
                    groups: Seq[AuthUser.GroupId] = Nil
                   ) {
  val isAdmin: Boolean = groups.contains(AuthUser.adminGroup)
  val isManager: Boolean = groups.contains(AuthUser.managerGroup)
  val isCustomer: Boolean = groups.contains(AuthUser.customerGroup)
}

object AuthUser {
  type UserId = String
  type GroupId = String

  val adminGroup = "admin"
  val managerGroup = "manager"
  val customerGroup = "customer"
  
  implicit val jsonFormat: OFormat[AuthUser] = derived.oformat[AuthUser]()

}

