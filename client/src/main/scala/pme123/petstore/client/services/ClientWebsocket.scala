package pme123.petstore.client.services

import com.thoughtworks.binding.Binding
import com.thoughtworks.binding.Binding.Var
import org.scalajs.dom._
import play.api.libs.json.{JsSuccess, Json}
import pme123.petstore.client.PetUIStore
import pme123.petstore.shared.{PathMsg, PetWebSocketMsg}

import scala.scalajs.js.timers.setTimeout

object ClientWebsocket
  extends ClientUtils {

  private lazy val wsURL =
    s"${
      window.location.protocol
        .replace("http", "ws")
    }//${window.location.host}${UIStore.uiState.webContext.value}/ws"

  private val producerWS: Var[Option[WebSocket]] = Var(None)
  private val consumerWS: Var[Option[WebSocket]] = Var(None)
  private val reconnectWSCode = 3001


  def connectProducerWS() {
    connectWS(
      () => {
        val ws = new WebSocket(s"$wsURL/pathMsgProducerWS")
        producerWS.value = Some(ws)
        ws
      },
      msg => info(s"Received from Producer Websocket: $msg"))
  }

  val connectConsumerWS = Binding {
    val user = UIStore.uiState.loggedInUser.bind.maybeUser
    user.foreach(u =>
      connectWS(
        () => {
          val ws = new WebSocket(s"$wsURL/pathMsgConsumerWS/${u.username}")
          consumerWS.value = Some(ws)
          ws
        },
        msg => Json.parse(msg).validate[PetWebSocketMsg] match {
          case JsSuccess(pathMsg: PathMsg, _) => PetUIStore.addPathMsg(pathMsg)
          case _ => info("Unexpected Message from Consumer")
        }
      ))
  }

  private def connectWS(createWs: () => WebSocket, onMessage: String => Any) {
    val socket = createWs()
    info(s"Connect to Websocket: ${socket.url}")
    socket.onmessage = {
      e: MessageEvent =>
        val message = e.data.toString
        info(s"Received from Websocket: $message")
        onMessage(message)
    }
    socket.onerror = { e: Event =>
      val ee = e.asInstanceOf[ErrorEvent]
      error(s"exception with websocket: ${ee.message}!")
      socket.close(0, ee.message)
    }
    socket.onopen = { _: Event =>
      info("websocket open!")
    }
    socket.onclose = { e: CloseEvent =>
      info("closed socket" + e.reason)
      if (e.code != reconnectWSCode) {
        setTimeout(1000) {
          connectWS(createWs, onMessage) // try to reconnect automatically
        }
      }
    }
  }

  def send(msg: String) {
    val username = UIStore.uiState.loggedInUser.value.username
    producerWS.value
      .foreach(_.send(Json.toJson(PathMsg(username, msg))
        .toString()))
  }

}
