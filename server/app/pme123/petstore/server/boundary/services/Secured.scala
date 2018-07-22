package pme123.petstore.server.boundary.services

import controllers.Assets
import javax.inject.Inject
import play.Environment
import play.api.Logger
import play.api.mvc.Results.Redirect
import play.api.mvc._
import pme123.petstore.shared.services.{AccessControl, AuthUser, Logging}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Security context that can be extended in order
  * to access the authenticated action-components.
  *
  * Support for:
  * - Basic HTTP authentication
  *
  * An implementor needs to implement:
  *
  * def isValidUser(user: String, pwd: String): Boolean
  *
  */
trait Secured
  extends Logging {

  import Secured._

  lazy val accessLogger = Logger("access-filter")

  def cc: ControllerComponents

  def env: Environment

  def accessControl: AccessControl

  implicit def ec: ExecutionContext

  /**
    * Request action builder that allows to add authentication
    * behavior to all specified actions.
    */
  def AuthenticatedAction: ActionBuilder[Request, AnyContent] = BPFActionBuilder(false)

  def AdminAction: ActionBuilder[Request, AnyContent] = BPFActionBuilder(true)

  case class BPFActionBuilder(isAdminAction: Boolean) extends ActionBuilder[Request, AnyContent] {

    def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] = {
      accessLogger.info(s"method=${request.method} uri=${request.uri} remote-address=${request.remoteAddress}")
      if (env.isTest) {
        block(request)
      } else {
        extractUser(request) match {
          case None =>
            Future.successful(Redirect(routes.AuthController.showLoginForm()))
          case Some(u) if !isAdminAction || (isAdminAction && u.isAdmin) =>
            block(request)
          case Some(u) =>
            warn(s"User $u has no admin rights for!")
            Future.successful(Assets.Unauthorized(s"You ($u) have no admin rights to access ${request.uri}!"))
        }
      }
    }

    def parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser

    protected def executionContext: ExecutionContext = cc.executionContext
  }

  class UserRequest[A](val username: Option[String], request: Request[A]) extends WrappedRequest[A](request)

  class UserAction @Inject()(val parser: BodyParsers.Default)(implicit val executionContext: ExecutionContext)
    extends ActionBuilder[UserRequest, AnyContent] with ActionTransformer[Request, UserRequest] {

    def transform[A](request: Request[A]): Future[UserRequest[A]] = Future.successful {
      new UserRequest(request.session.get("username"), request)
    }
  }

  // extracts the username from the Header
  protected def extractUser(implicit request: RequestHeader): Option[AuthUser] =

    request.session.get(SESSION_USERNAME_KEY).map { userId =>
      val groups =
        request.session.get(SESSION_GROUPS_KEY)
          .map(_.split(SESSION_GROUPS_SEPARATOR).toSeq).getOrElse(Nil)

      AuthUser(userId, groups)
    }

}

object Secured {
  val SESSION_USERNAME_KEY = "username"
  val SESSION_GROUPS_KEY = "groups"
  val SESSION_GROUPS_SEPARATOR = ","

}