package pme123.petstore.client.services

import com.thoughtworks.binding.Binding.Var
import org.scalajs.dom._
import play.api.libs.json.Json
import pme123.petstore.shared.services.PathMsg

import scala.scalajs.js.timers.setTimeout

object ClientWebsocket
  extends ClientUtils {

  private lazy val wsURL =
    s"${
      window.location.protocol
        .replace("http", "ws")
    }//${window.location.host}${UIStore.uiState.webContext.value}/ws/client"

  private val webSocket: Var[Option[WebSocket]] = Var(None)
  private val reconnectWSCode = 3001


  def connectWS() {
    closeWS()
    val path = s"$wsURL"
    val socket = new WebSocket(path)
    webSocket.value = Some(socket)
    info(s"Connect to Websocket: $path")
    socket.onmessage = {
      e: MessageEvent =>
        val message = Json.parse(e.data.toString)
        /*message.validate[AdapterMsg] match {
          case JsSuccess(AdapterRunning(logReport), _) =>
            UIStore.changeIsRunning(true)
            UIStore.addLogReport(logReport)
          case JsSuccess(other, _) =>
            info(s"Other message: $other")
          case JsError(errors) =>
            errors.foreach(e => error(e.toString))
        }*/
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
          connectWS() // try to reconnect automatically
        }
      }
    }
  }

  def send(msg: String) {
    val username = UIStore.uiState.loggedInUser.value.username
    webSocket.value
      .foreach(_.send(Json.toJson(PathMsg(username, msg))
        .toString()))
  }

  def closeWS(): Unit = {
    webSocket.value.foreach(_.close(reconnectWSCode, ": Reconnect for different configuration."))
  }

}
