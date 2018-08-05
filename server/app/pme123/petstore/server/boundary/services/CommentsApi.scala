package pme123.petstore.server.boundary.services

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.control.services.CommentsRepo

import scala.concurrent.{ExecutionContext, Future}

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class CommentsApi @Inject()(val spaComps: SPAComponents)
                           (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  def commentsFor(username: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val comments = CommentsRepo.commentsForUser(username)

    Future.successful(
      Ok(Json.toJson(comments)).as(JSON)
    )
  }
}


