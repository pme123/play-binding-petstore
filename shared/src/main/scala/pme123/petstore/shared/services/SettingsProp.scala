package pme123.petstore.shared.services

import julienrf.json.derived
import play.api.libs.json.OFormat


case class SettingsProp(key: String, value: String) {

  def asString(name: String): String =
    propString(name)

  def propString(name: String) =
    s"${name.toUpperCase} '$key' >> $value"

}

object SettingsProp {
  implicit val jsonFormat: OFormat[SettingsProp] = derived.oformat[SettingsProp]()

}
