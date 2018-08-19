package pme123.petstore.server.boundary

import akka.NotUsed
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import javax.inject.Inject
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.boundary.services.{SPAComponents, SPAController}
import pme123.petstore.server.control.services.SameOriginCheck
import pme123.petstore.server.control.{PathMsgConsumer, PathMsgProducer}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class WebsocketController @Inject()(pathMsgProducer: PathMsgProducer,
                                    pathMsgConsumer: PathMsgConsumer,
                                    comps: SPAComponents)
                                   (implicit val ec: ExecutionContext)
  extends SPAController(comps)
    with SameOriginCheck {

  implicit val timeout: Timeout = Timeout(1.second) // the first run in dev can take a while :-(

  def pathMsgProducerWS(): WebSocket =
    webSocket(() => pathMsgProducer.producerFlow())

  def pathMsgConsumerWS(userId: String): WebSocket =
    webSocket(() => pathMsgConsumer.consumerFlow(userId))

  def webSocket(wsFlow: () => Future[Flow[JsValue, JsValue, NotUsed]]): WebSocket =
    WebSocket.acceptOrResult[JsValue, JsValue] {
      case rh if sameOriginCheck(rh) =>
        wsFlow().map { flow =>
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

}