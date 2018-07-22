package pme123.petstore.server.boundary.services

import javax.inject.Inject
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._
import pme123.petstore.server.boundary.services
import pme123.petstore.server.entity.Identity
import pme123.petstore.shared.services.AccessControl

import scala.concurrent.ExecutionContext

class AuthController @Inject()(accessControl: AccessControl,
                               userLoginTempl: views.html.login,
                               val spaComps: SPAComponents)
                              (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  import Secured._

  val form: Form[Identity] = Form(
    mapping(
      "username" -> nonEmptyText
        .verifying("too few chars", s => lengthIsGreaterThanNCharacters(s, 2))
        .verifying("too many chars", s => lengthIsLessThanNCharacters(s, 20)),
      "password" -> nonEmptyText
        .verifying("too few chars", s => lengthIsGreaterThanNCharacters(s, 2))
        .verifying("too many chars", s => lengthIsLessThanNCharacters(s, 30)),
    )(Identity.apply)(Identity.unapply)
  )

  private val formSubmitUrl = routes.AuthController.processLoginAttempt()

  def showLoginForm: Action[AnyContent] = Action.async {
    implicit request =>
      pageConfig(extractUser(request))
        .map(pc => Ok(userLoginTempl(pc, form, formSubmitUrl)))
  }

  def processLoginAttempt: Action[AnyContent] = Action.async { implicit request =>
    pageConfig(extractUser(request))
      .map { projConf =>

        val errorFunction: Form[Identity] => Result = {
          formWithErrors: Form[Identity] =>
            // form validation/binding failed...
            BadRequest(userLoginTempl(projConf, formWithErrors, formSubmitUrl))
        }
        val successFunction = {
          user: Identity =>
            // form validation/binding succeeded ...
            val foundUser = accessControl.isValidUser(user.username, user.password)
            if (foundUser) {
              val authUser = accessControl.getUser(user.username)
              Redirect(routes.HomeController.index())
                .flashing("info" -> "You are logged in.")
                .withSession((SESSION_USERNAME_KEY, user.username),
                  (SESSION_GROUPS_KEY, authUser.groups.mkString(SESSION_GROUPS_SEPARATOR))
                )
            } else {
              Redirect(services.routes.AuthController.showLoginForm())
                .flashing("error" -> "Invalid username/password.")
            }
        }
        val formValidationResult: Form[Identity] = form.bindFromRequest
        formValidationResult.fold(
          errorFunction,
          successFunction
        )
      }
  }

  def logout = Action {
    implicit request: Request[AnyContent] =>
      // docs: “withNewSession ‘discards the whole (old) session’”
      Redirect(routes.AuthController.showLoginForm())
        .flashing("info" -> "Sie sind ausgeloggt.")
        .withNewSession
  }

  private def lengthIsGreaterThanNCharacters(s: String, n: Int): Boolean = {
    if (s.length > n) true else false
  }

  private def lengthIsLessThanNCharacters(s: String, n: Int): Boolean = {
    if (s.length < n) true else false
  }
}