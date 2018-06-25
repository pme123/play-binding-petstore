package pme123.petstore.server.control.services

import akka.actor._
import akka.event.LoggingReceive
import akka.util.Timeout
import javax.inject.{Inject, Named}
import play.api.Configuration
import play.api.libs.concurrent.InjectedActorSupport
import pme123.petstore.shared.services.Logging

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
/**
 * Provide some DI and configuration sugar for new UserActor instances.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
class ClientParentActor @Inject()(@Named("jobParentActor")
                                  jobParentActor: ActorRef
                                  , childFactory: ClientActor.Factory,
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
    case RegisterClient() => registerClient()
  }

  // 2. level of abstraction
  // **************************

  private def registerClient() = {
    val name = s"clientActor-${sender().path}"
    info(s"Creating ClientActor $name")

     //TODO when doing websockets!
  }


}

object ClientParentActor {

  case class RegisterClient()

  case object GetClientConfigs

}
