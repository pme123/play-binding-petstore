package pme123.petstore.client.services

import pme123.petstore.shared.services.Logging
import slogging.{ConsoleLoggerFactory, LoggerConfig}

trait SPAClient
  extends ClientUtils
    with Logging {

  LoggerConfig.factory = ConsoleLoggerFactory()

  def initClient(context: String
                 , webPath: String): Unit = {
    info(s"Init client with: $context$webPath")
    UIStore.changeWebContext(context)
    UIStore.changeWebPath(webPath)
    ClientWebsocket.connectWS()
  }
}
