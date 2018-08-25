import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import pme123.petstore.server.control.{ExcelImporter, PetDBInitializer}
import slogging.{LoggerConfig, SLF4JLoggerFactory}

class Module extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit = {
    // framework
    LoggerConfig.factory = SLF4JLoggerFactory()

    bind(classOf[PetDBInitializer])
      .asEagerSingleton()

    bind(classOf[ExcelImporter])
      .asEagerSingleton()

  }
}
