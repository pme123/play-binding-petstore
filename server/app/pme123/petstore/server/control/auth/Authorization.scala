package pme123.petstore.server.control.auth

import com.mohiva.play.silhouette.api.Authorization
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import play.api.mvc.Request
import pme123.petstore.server.entity.AuthUser

import scala.concurrent.Future


/**
  * Only allows those users that have at least a service of the selected.
  * Master service is always allowed.
  * Ex: RestrictAdmin() => only users with role "Admin" are allowed!
  */
case class RestrictAdmin() extends Authorization[AuthUser, CookieAuthenticator] {
  def isAuthorized[A](user: AuthUser, authenticator: CookieAuthenticator)(implicit r: Request[A]): Future[Boolean] = Future.successful {
    user.isAdmin
  }
}

/**
  * Only allows those users that have at least a service of the selected.
  * Master service is always allowed.
  * Ex: WithRole("roleA", "roleB") => only users with roles "roleA" OR "roleB" (or "master") are allowed.
  */
case class WithRole(anyOf: String*) extends Authorization[AuthUser, CookieAuthenticator] {
  def isAuthorized[A](user: AuthUser, authenticator: CookieAuthenticator)(implicit r: Request[A]): Future[Boolean] = Future.successful {
    WithRole.isAuthorized(user, anyOf: _*)
  }
}

object WithRole {
  def isAuthorized(user: AuthUser, anyOf: String*): Boolean =
    anyOf.intersect(user.groups.toSeq).nonEmpty || user.groups.contains("admin")
}

/**
  * Only allows those users that have every of the selected roles.
  * admin role is always allowed.
  * Ex: WithRoles("roleA", "roleB") => only users with roles "roleA" AND "roleB" (or "master") are allowed.
  */
case class WithRoles(allOf: String*) extends Authorization[AuthUser, CookieAuthenticator] {
  def isAuthorized[A](user: AuthUser, authenticator: CookieAuthenticator)(implicit r: Request[A]): Future[Boolean] = Future.successful {
    WithRoles.isAuthorized(user, allOf: _*)
  }
}

object WithRoles {
  def isAuthorized(user: AuthUser, allOf: String*): Boolean =
    allOf.intersect(user.groups.toSeq).size == allOf.size || user.groups.contains("admin")
}