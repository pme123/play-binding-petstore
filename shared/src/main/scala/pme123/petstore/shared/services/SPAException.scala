package pme123.petstore.shared.services

/**
  * general exception to mark a handled exception
  */
trait SPAException
  extends RuntimeException {
  def msg: String

  def cause: Option[Throwable] = None

  override def getMessage: String = msg

  override def getCause: Throwable = {
    cause.orNull
  }
}

