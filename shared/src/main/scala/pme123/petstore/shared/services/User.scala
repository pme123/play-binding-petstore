package pme123.petstore.shared.services

import julienrf.json.derived
import play.api.libs.json.OFormat
import pme123.petstore.shared.services.Language.DE

case class User(id: User.UserId,
                groups: Seq[User.GroupId] = Nil,
                firstName: String,
                lastName: String,
                email: String,
                avatar: String,
                language: Language = DE) {
  lazy val fullName = s"$firstName $lastName"

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


  implicit val jsonFormat: OFormat[User] = derived.oformat[User]()

}



