package pme123.petstore.server.entity

import pme123.petstore.shared.services.SPAException

/**
  * Marker trait for all internal Exceptions, that are handled.
  * Created by pascal.mengelt on 09.08.2016.
  */
case class JsonParseException(msg: String, override val cause: Option[Throwable] = None)
  extends SPAException {
}

case class ServiceException(msg: String, override val cause: Option[Throwable] = None)
  extends SPAException

case class ConfigException(msg: String, override val cause: Option[Throwable] = None)
  extends SPAException

