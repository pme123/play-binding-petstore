import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import pme123.petstore.server.control.services.{ClientActor, ClientParentActor}
import slogging.{LoggerConfig, SLF4JLoggerFactory}

class Module extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit = {
    // framework
    LoggerConfig.factory = SLF4JLoggerFactory()

    bindActor[ClientParentActor]("clientParentActor")
    bindActorFactory[ClientActor, ClientActor.Factory]

  }
}
