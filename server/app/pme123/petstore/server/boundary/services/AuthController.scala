package pme123.petstore.server.boundary.services

import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.util.{Clock, Credentials}
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticator
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import javax.inject.Inject
import play.api.i18n.Messages
import play.api.mvc._
import pme123.petstore.server.control.auth.{IdentityService, UserService}

import scala.concurrent.{ExecutionContext, Future}

/**
  * @see https://www.playframework.com/documentation/2.6.x/ScalaForms#Passing-MessagesProvider-to-Form-Helpers
  */
class AuthController @Inject()(identityApi:IdentityService,
                               userService: UserService,
                               signInTempl: views.html.login,
                               clock: Clock,
                               val comps: SPAComponents)
                              (implicit val ec: ExecutionContext)
  extends SPAController(comps) {

  def signIn(redirectUrl: Option[String] = None): Action[AnyContent] = UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(_) => Future.successful(Redirect(routes.HomeController.index()))
      case None =>
        val signInForm = SignInForm.form
        pageConfig(None).map(conf=>
        Ok(signInTempl(signInForm, redirectUrl, conf)))
    }
  }

  /**
    * Authenticates the user based on his username and password
    */
  def authenticate(redirectUrl: Option[String] = None): Action[AnyContent] = UnsecuredAction.async { implicit request =>
    SignInForm.form.bindFromRequest.fold(
      formWithErrors =>pageConfig(None).map(conf=>
        BadRequest(signInTempl(formWithErrors, redirectUrl, conf))),
      formData => {
        val authService = silhouette.env.authenticatorService
        verifyCredentials(Credentials(formData.username, formData.password)).flatMap { loginInfo =>
          userService.retrieve(loginInfo).flatMap {
            case Some(user) => for {
              authenticator <- authService.create(loginInfo).map(authenticatorWithRememberMe(_, formData.rememberMe))
              cookie <- authService.init(authenticator)
              result <- authService.embed(cookie, redirectResult(redirectUrl))
            } yield {
              silhouette.env.eventBus.publish(LoginEvent(user, request))
              result
            }
            case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
          }
        }.recover {
          case _: ProviderException => Redirect(routes.AuthController.signIn(redirectUrl)).flashing("error" -> Messages("auth.credentials.incorrect"))
        }
      }
    )
  }

  /**
    * Signs out the user
    */
  def signOut(redirectUrl: Option[String] = None): Action[AnyContent] = SecuredAction.async { implicit request =>
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    val authService = silhouette.env.authenticatorService
    authService.discard(request.authenticator, redirectResult(redirectUrl))
  }

  private def redirectResult(redirectUrl: Option[String] = None): Result =
    redirectUrl.map(Redirect(_)).getOrElse(Redirect(routes.HomeController.index()))

  private def verifyCredentials(credentials: Credentials): Future[LoginInfo] = {
    if (identityApi.isValidUser(credentials.identifier, credentials.password))
      Future.successful(LoginInfo(credentials.identifier, userService.providerKey))
    else
      throw new ProviderException("Wrong Username or Password")
  }

  private def authenticatorWithRememberMe(authenticator: CookieAuthenticator, rememberMe: Boolean) = {
    if (rememberMe) {
      val config = comps.config
      authenticator.copy(
        expirationDateTime = clock.now.withDurationAdded(config.authenticatorExpiry.toMillis, 1),
        idleTimeout = config.authenticatorIdleTimeout,
        cookieMaxAge = config.cookieMaxAge
      )
    } else
      authenticator.copy()
  }

}

import play.api.data.Form
import play.api.data.Forms._

object SignInForm {

  val form = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "rememberMe" -> boolean
    )(Data.apply)(Data.unapply)
  )

  case class Data(username: String,
                  password: String,
                  rememberMe: Boolean)

}

