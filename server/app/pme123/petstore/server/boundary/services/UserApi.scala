package pme123.petstore.server.boundary.services

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.control.services.UserRepo

import scala.concurrent.{ExecutionContext, Future}

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class UserApi @Inject()(val spaComps: SPAComponents)
                       (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  def loggedInUser(): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    val user = extractUser.map(UserRepo.userFor)

    Future.successful(
      Ok(Json.toJson(user)).as(JSON)
    )
  }
}

object UserApi {

}
