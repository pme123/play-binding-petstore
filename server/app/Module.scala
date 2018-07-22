import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import pme123.petstore.server.boundary.PetstoreAccessControl
import pme123.petstore.server.boundary.services.NoAccessControl
import pme123.petstore.server.control.services.{ClientActor, ClientParentActor}
import pme123.petstore.shared.services.AccessControl
import slogging.{LoggerConfig, SLF4JLoggerFactory}

class Module extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit = {
    // framework
    LoggerConfig.factory = SLF4JLoggerFactory()

    // you need to define the AccessControl
    bind(classOf[AccessControl])
      .to(classOf[PetstoreAccessControl])

    bindActor[ClientParentActor]("clientParentActor")
    bindActorFactory[ClientActor, ClientActor.Factory]

  }
}
