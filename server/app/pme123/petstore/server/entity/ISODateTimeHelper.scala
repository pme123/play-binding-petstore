package pme123.petstore.server.entity

import java.time._
import java.time.format.DateTimeFormatter

/**
  * Created by pascal.mengelt on 09.03.2015.
  *
  */
trait   ISODateTimeHelper {
  val isoPattern = "yyyy-MM-dd'T'HH:mm"
  val isoFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(isoPattern)

  def timezone: ZoneId

  def toLocalDateTime(isoDateTimeStr: String): LocalDateTime = {

    LocalDateTime.parse(isoDateTimeStr, isoFormatter)
  }

  def toISODateTimeString(dateTime: LocalDateTime): String =
    isoFormatter.format(dateTime)
}
