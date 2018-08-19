import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport
import slogging.{LoggerConfig, SLF4JLoggerFactory}

class Module extends AbstractModule with AkkaGuiceSupport {

  override def configure(): Unit = {
    // framework
    LoggerConfig.factory = SLF4JLoggerFactory()

  }
}
