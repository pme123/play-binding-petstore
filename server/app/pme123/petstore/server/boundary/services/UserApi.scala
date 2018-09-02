package pme123.petstore.server.boundary.services

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.control.auth.DefaultEnv
import pme123.petstore.server.control.services.UserDBRepo
import pme123.petstore.server.entity.AuthUser
import pme123.petstore.shared.services.LoggedInUser

import scala.concurrent.ExecutionContext

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class UserApi @Inject()(val spaComps: SPAComponents
                       ,userDBRepo: UserDBRepo)
                       (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  def loggedInUser(): Action[AnyContent] = SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    withUser()
        .map(u => Ok(Json.toJson(LoggedInUser(u))).as(JSON))

  }

  private def withUser()(implicit user: AuthUser) = {
    userDBRepo.findUser(user.id)
  }
}

object UserApi {

}
