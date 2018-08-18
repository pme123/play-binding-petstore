package pme123.petstore.server.boundary.services

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.control.auth.DefaultEnv
import pme123.petstore.server.control.services.UserRepo
import pme123.petstore.server.entity.AuthUser

import scala.concurrent.{ExecutionContext, Future}

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class UserApi @Inject()(val spaComps: SPAComponents)
                       (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  def loggedInUser(): Action[AnyContent] = SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>

    Future.successful(
      Ok(Json.toJson(withUser())).as(JSON)
    )
  }

  private def withUser()(implicit user: AuthUser) = {
    UserRepo.userFor(user)
  }
}

object UserApi {

}
