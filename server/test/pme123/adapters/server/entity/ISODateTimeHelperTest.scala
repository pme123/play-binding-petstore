package pme123.adapters.server.entity

import java.time.ZoneId

import pme123.petstore.server.entity.ISODateTimeHelper

class ISODateTimeHelperTest
  extends UnitTest
    with ISODateTimeHelper {

  val timezone: ZoneId = ZoneId.of("Europe/Zurich")


  "An ISO DateTime String" should "be the same after creating a LocalDateTime" in {
    val isoDate = "2018-01-13T12:33"
    toISODateTimeString(
      toLocalDateTime(isoDate)
    ) should be(isoDate)
  }
}
