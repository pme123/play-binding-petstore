package pme123.petstore.server.boundary

import akka.NotUsed
import akka.actor.ActorRef
import akka.pattern.ask
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import javax.inject.{Inject, Named}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import pme123.petstore.server.control.services.ClientParentActor.RegisterClient
import pme123.petstore.server.control.services.SameOriginCheck

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class WebsocketController @Inject()(@Named("clientParentActor")
                                    clientParentActor: ActorRef,
                                    pc: SPAComponents)
                                   (implicit ec: ExecutionContext)
  extends SPAController(pc)
    with SameOriginCheck {

  implicit val timeout: Timeout = Timeout(1.second) // the first run in dev can take a while :-(

  /**
    * Creates a websocket.  `acceptOrResult` is preferable here because it returns a
    * Future[Flow], which is required internally.
    *
    * @return a fully realized websocket.
    */
  def ws(webPath: Option[String]
         , resultCount: Option[Int]
         , resultFilter: Option[String]): WebSocket = websocket()

  def websocket(): WebSocket = WebSocket.acceptOrResult[JsValue, JsValue] {
    case rh if sameOriginCheck(rh) =>
      wsFutureFlow().map { flow =>
        Right(flow)
      }.recover {
        case e: Exception =>
          error("Cannot create websocket", e)
          val jsError = Json.obj("error" -> "Cannot create websocket")
          val result = InternalServerError(jsError)
          Left(result)
      }
    case rejected =>
      error(s"Request $rejected failed same origin check")
      Future.successful {
        Left(Forbidden("forbidden"))
      }
  }

  /**
    * Creates a Future containing a Flow of JsValue in and out.
    */
  private def wsFutureFlow(): Future[Flow[JsValue, JsValue, NotUsed]] = {
    // Use guice assisted injection to instantiate and configure the child actor.
    val future: Future[Any] = clientParentActor ? RegisterClient()
    val futureFlow: Future[Flow[JsValue, JsValue, NotUsed]] = future.mapTo[Flow[JsValue, JsValue, NotUsed]]
    futureFlow
  }

}