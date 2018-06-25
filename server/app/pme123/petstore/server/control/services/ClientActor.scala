package pme123.petstore.server.control.services

import akka.actor._
import akka.stream._
import akka.util.Timeout
import javax.inject._
import pme123.petstore.server.entity.ActorMessages.InitActor

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
  * Creates a initiator actor that sets up the websocket stream.  Although it's not required,
  * having an actor manage the stream helps with lifecycle and monitoring, and also helps
  * with dependency injection through the AkkaGuiceSupport trait.
  *
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  *
  * @param ec       implicit CPU bound execution context.
  */
class ClientActor @Inject()()
                           (implicit val mat: Materializer, val ec: ExecutionContext)
  extends ClientWebsocket {
  implicit private val timeout: Timeout = Timeout(50.millis)


  // 1. level of abstraction
  // **************************

  override def receive: Receive = {
    case InitActor => init()
    case other =>
      info(s"Unexpected message from ${sender()}: $other")
  }

  /**
    * If this actor is killed directly, stop anything that we started running explicitly.
    * In our case unsubscribe the client in the AdapterActor
    */
  override def postStop() {
    info(s"Stopping ${self.path}")
  }

  // 2. level of abstraction
  // **************************
  private def init() {
    info(s"Create Websocket for Client: ${self.path}")
    sender() ! websocketFlow()
  }

}

object ClientActor {

  // used to inject the UserActors as childs of the UserParentActor
  trait Factory {
    def apply(): Actor
  }

  case object GetClientConfig

}



