package pme123.petstore.server.boundary.services

import pme123.petstore.shared.services.{AccessControl, AuthUser}

// no access control needed
class NoAccessControl extends AccessControl {

  def isValidUser(user: String, pwd: String): Boolean = true

  def getUser(username: String): AuthUser = AuthUser(username)
}
