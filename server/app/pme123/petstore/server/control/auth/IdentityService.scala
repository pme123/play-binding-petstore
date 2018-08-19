package pme123.petstore.server.control.auth

import pme123.petstore.server.control.services.UserRepo
import pme123.petstore.server.entity.AuthUser
import pme123.petstore.shared.services.User.UserId

class IdentityService {

  def isValidUser(username: UserId, pwd: String): Boolean =
    UserRepo.contains(username) // no password required

  def getUser(username: UserId): AuthUser =
    UserRepo.authUser(username)
}
