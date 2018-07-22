package pme123.petstore.shared.services

import enumeratum.{Enum, EnumEntry}
import julienrf.json.derived
import play.api.libs.json.OFormat

import scala.collection.immutable.IndexedSeq

sealed trait Language extends EnumEntry {

  def label: String

  def abbreviation: String

  def labelUppercase: String = label.toUpperCase

  def labelLowercase: String = label.toLowerCase

  def abbreviationUppercase: String = abbreviation.toUpperCase
}


// see https://github.com/lloydmeta/enumeratum#usage
object Language
  extends Enum[Language] {

  val values: IndexedSeq[Language] = findValues

  case object DE extends Language {
    override def label: String = "Deutsch"

    override def abbreviation: String = "de"
  }

  case object EN extends Language {
    override def label: String = "English"

    override def abbreviation: String = "en"
  }

  case object FR extends Language {
    override def label: String = "Française"

    override def abbreviation: String = "fr"
  }

  case object IT extends Language {
    override def label: String = "Italiano"

    override def abbreviation: String = "it"
  }

  implicit val jsonFormat: OFormat[Language] = derived.oformat[Language]()

}
