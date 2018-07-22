package pme123.petstore.shared.services

/**
  * interface for a secured access to the boundary.
  * At the moment only 'username' - 'password' is supported.
  */
trait AccessControl {
  /**
    * A user is valid for authentication if:
    * - is the 'importer' user
    * - is a user with the 'admin' role
    *
    * @param username username
    * @param pwd  plain password
    * @return valid logged user for importing
    */
  def isValidUser(username: String, pwd: String): Boolean

  def getUser(username: String): AuthUser
}
