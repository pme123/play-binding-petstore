package pme123.petstore.server.control.auth

import com.mohiva.play.silhouette.api.actions.{SecuredRequest, UserAwareRequest}
import com.mohiva.play.silhouette.api.{Env, Silhouette}

//noinspection TypeAnnotation
trait SecuredController[E <: Env] {
  def silhouette: Silhouette[E]

  def SecuredAction = silhouette.SecuredAction
  def UnsecuredAction = silhouette.UnsecuredAction
  def UserAwareAction = silhouette.UserAwareAction

  implicit def securedRequest2User[A](implicit request: SecuredRequest[E, A]): E#I = request.identity
  implicit def userAwareRequest2UserOpt[A](implicit request: UserAwareRequest[E, A]): Option[E#I] = request.identity
}