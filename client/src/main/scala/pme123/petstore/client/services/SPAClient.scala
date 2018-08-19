package pme123.petstore.client.services

import com.thoughtworks.binding.Binding
import pme123.petstore.shared.services.Logging
import slogging.{ConsoleLoggerFactory, LoggerConfig}

trait SPAClient
  extends ClientUtils
    with Logging {

  LoggerConfig.factory = ConsoleLoggerFactory()

  def initClient(context: String): Unit = {
    info(s"Init client with Context: $context")
    UIStore.changeWebContext(context)
    ClientWebsocket.connectProducerWS()
  }
}
