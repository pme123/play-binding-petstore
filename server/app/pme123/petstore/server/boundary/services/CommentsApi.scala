package pme123.petstore.server.boundary.services

import doobie.implicits._
import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.control.services.UserDBRepo
import pme123.petstore.shared.services.{Conversation, Conversations, NewComment}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class CommentsApi @Inject()(val spaComps: SPAComponents,
                            userDBRepo: UserDBRepo)
                           (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  def commentsFor(username: String): Action[AnyContent] = SecuredAction.async { implicit request: Request[AnyContent] =>

    commentsForUser(username)
      .map(cs => Ok(Json.toJson(cs)).as(JSON))
  }

  def addComment(): Action[AnyContent] = Action.async { implicit request =>
    val body = request.body
    body.asJson match {
      case Some(newComent) => newComent.validate[NewComment] match {
        case JsSuccess(comment: NewComment, path) =>
          for {
            _ <- userDBRepo.insertComment(comment)
            comments <- commentsForUser("demoCustomer")
            _ = println(s"comments: $comments")
          } yield Ok(Json.toJson(comments)).as(JSON)
      }


      case None => Future.successful(BadRequest("No Comment set"))
    }
  }

  private def commentsForUser(username: String) = {
    for {
      maybeUser <- userDBRepo.findUser(username)
      if maybeUser.nonEmpty
      comments <- userDBRepo.selectComments(fr"where c.username = $username")
    } yield Conversations(Seq())

  }
}


