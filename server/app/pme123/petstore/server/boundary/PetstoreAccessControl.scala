package pme123.petstore.server.boundary

import pme123.petstore.server.control.services.UserRepo
import pme123.petstore.shared.services.AuthUser.UserId
import pme123.petstore.shared.services.{AccessControl, AuthUser}

class PetstoreAccessControl extends AccessControl{

  def isValidUser(username: UserId, pwd: String): Boolean =
    UserRepo.contains(username) // no password required

  def getUser(username: UserId): AuthUser =
    UserRepo.authUser(username)
}
