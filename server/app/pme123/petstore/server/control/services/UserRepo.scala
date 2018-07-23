package pme123.petstore.server.control.services

import pme123.petstore.shared.services.Language.{DE, EN}
import pme123.petstore.shared.services.{AuthUser, User}

object UserRepo {

  import AuthUser._

  private val demoCustomer = "demoCustomer"
  private val demoManager = "demoManager"
  private val demoAdmin = "demoAdmin"

  val authUsers = Map(
    demoCustomer -> AuthUser(demoCustomer, Seq(customerGroup)),
    demoManager -> AuthUser(demoManager, Seq(managerGroup)),
    demoAdmin -> AuthUser(demoAdmin, Seq(adminGroup))
  )

  val users = Map(
    demoCustomer -> User(authUsers(demoCustomer), "Peter", "Caprio", "peter@caprio.com", "caprio.jpg", EN),
    demoManager -> User(authUsers(demoManager), "Kent", "Reeves", "kent@petstore.com", "reeves.jpg", EN),
    demoAdmin -> User(authUsers(demoAdmin), "Daniel", "Devito", "daniel@petstore.com", "devito.jpg", DE)
  )

  def contains(username: UserId): Boolean =
    authUsers.keys.toSeq.contains(username)


  def authUser(username:UserId): AuthUser =
    authUsers(username)

  def userFor(authUser: AuthUser):User =
    users(authUser.id)

}
