package pme123.petstore.server.control.services

import akka.actor._
import akka.event.LoggingReceive
import akka.pattern.{ask, pipe}
import akka.stream.scaladsl.Flow
import akka.util.Timeout
import javax.inject.Inject
import play.api.Configuration
import play.api.libs.concurrent.InjectedActorSupport
import play.api.libs.json.JsValue
import pme123.petstore.server.entity.ActorMessages.InitActor
import pme123.petstore.shared.services.Logging
import pme123.petstore.shared.services.PathMsg.Username

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Provide some DI and configuration sugar for new UserActor instances.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
class ClientParentActor @Inject()(childFactory: ClientActor.Factory,
                                  configuration: Configuration)
                                 (implicit ec: ExecutionContext)
  extends Actor
    with InjectedActorSupport
    with Logging {

  import ClientParentActor._

  implicit private val timeout: Timeout = Timeout(2.seconds)

  // 1. level of abstraction
  // **************************
  override def receive: Receive = LoggingReceive {
    case RegisterClient(username) => registerClient(username)
  }

  // 2. level of abstraction
  // **************************

  private def registerClient(username: Username) = {
    val name = s"clientActor-$username"
    info(s"Creating ClientActor $name")
    val child: ActorRef = injectedChild(childFactory(), name)
    val future =
      (child ? InitActor).mapTo[Flow[JsValue, JsValue, _]]
        .recover {
          case exc: Exception =>
            error(s"Problem create ClientActor: $username", exc)
        }
    pipe(future) to sender()
  }


}

object ClientParentActor {

  case class RegisterClient(username: Username)

  case object GetClientConfigs

}
