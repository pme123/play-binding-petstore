package pme123.petstore.server.control.services

import pme123.petstore.server.entity.AuthUser
import pme123.petstore.shared.services.Language.{DE, EN}
import pme123.petstore.shared.services.User

object UserRepo {

  import User._

  val demoCustomer = "demoCustomer"
  val demoManager = "demoManager"
  val demoAdmin = "demoAdmin"

  val authUsers = Map(
    demoCustomer -> AuthUser(demoCustomer, Seq(customerGroup)),
    demoManager -> AuthUser(demoManager, Seq(managerGroup)),
    demoAdmin -> AuthUser(demoAdmin, Seq(adminGroup))
  )

  val users = Map(
    demoCustomer -> User(demoCustomer, Seq(customerGroup), "Peter", "Caprio", "peter@caprio.com", "caprio.jpg", EN),
    demoManager -> User(demoManager, Seq(managerGroup), "Kent", "Reeves", "kent@petstore.com", "reeves.jpg", EN),
    demoAdmin -> User(demoAdmin, Seq(adminGroup), "Daniel", "Devito", "daniel@petstore.com", "devito.jpg", DE)
  )

  def contains(username: UserId): Boolean =
    authUsers.keys.toSeq.contains(username)


  def authUser(username: UserId): AuthUser =
    authUsers(username)

  def userFor(authUser: AuthUser): User =
    users(authUser.id)

  def userFor(username: String): User =
    users(username)

}
