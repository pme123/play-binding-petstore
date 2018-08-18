package pme123.petstore.server.control.auth

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import pme123.petstore.server.entity.AuthUser

trait DefaultEnv extends Env {
  type I = AuthUser
  type A = CookieAuthenticator
}