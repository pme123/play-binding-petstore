package pme123.petstore.shared.services

import java.time.Instant

import enumeratum.{Enum, EnumEntry}
import slogging.LazyLogging

import scala.collection.immutable.IndexedSeq

/**
  *
  * Encapsulates Logging - so if changed here is the only place to adjust.
  *
  * Supported Levels are:
  * - DEBUG
  * - INFO
  * - WARN
  * - ERROR
  */
trait Logging
  extends LazyLogging {

  private lazy val underlying = logger.underlying

  def debug(msg: String): String = {
    if (underlying.isDebugEnabled)
      logger.debug(msg)
    msg
  }

  def info(msg: String): String = {
    if (underlying.isInfoEnabled)
      logger.info(msg)
    msg
  }

  def warn(msg: String): String = {
    if (underlying.isWarnEnabled)
      logger.warn(msg)
    msg
  }

  def error(msg: String): String = {
    if (underlying.isErrorEnabled)
      logger.error(msg)
    msg
  }

  def error(exc: Throwable): String = {
    if (underlying.isErrorEnabled)
      logger.error(exc.getMessage, exc)
    exc.getMessage
  }

  def error(msg: String, exc: Throwable): String = {
    if (underlying.isErrorEnabled)
      logger.error(msg, exc)
    msg
  }

  def exceptionToString(exc: Throwable): String = {
    def inner(throwable: Throwable, last: Throwable): String =
      if (throwable == null || throwable == last) ""
      else {
        val causeMsg = inner(throwable.getCause, throwable)
        val msg = s"${throwable.getMessage} [${throwable.getStackTrace.headOption.map(_.toString).getOrElse("No stack trace")}]"
        msg + (if (causeMsg.nonEmpty) s"\n - Cause: $causeMsg" else "")
      }

    inner(exc, null)
  }

  def startLog(msg: String): Instant = {
    val dateTime = Instant.now
    info(s"$dateTime start: msg")
    dateTime
  }

  def endLog(msg: String, startDate: Instant): String = {
    info(s"Finished after ${Instant.now().toEpochMilli - startDate.toEpochMilli} ms: $msg")
  }
}

object Logging extends Logging

sealed trait LogLevel
  extends EnumEntry {
  def level: String

  def colorClass: String

  def >=(level: LogLevel): Boolean

  def asHtmlString = s"""<span class="$colorClass">${level.toUpperCase}</span>"""

}


object LogLevel
  extends Enum[LogLevel] {

  val values: IndexedSeq[LogLevel] = findValues

  case object DEBUG extends LogLevel {
    val level = "debug"
    val colorClass = "grey"

    override def >=(level: LogLevel): Boolean = level match {
      case DEBUG => true
      case _ => false
    }
  }

  case object INFO extends LogLevel {
    val level = "info"
    val colorClass = "black"

    override def >=(level: LogLevel): Boolean = level match {
      case DEBUG => true
      case INFO => true
      case _ => false
    }
  }

  case object WARN extends LogLevel {

    val level = "warn"
    val colorClass = "yellow"

    override def >=(level: LogLevel): Boolean = level match {
      case ERROR => false
      case _ => true
    }

  }

  case object ERROR extends LogLevel {
    val level = "error"
    val colorClass = "red"

    override def >=(level: LogLevel): Boolean = true

  }

}
