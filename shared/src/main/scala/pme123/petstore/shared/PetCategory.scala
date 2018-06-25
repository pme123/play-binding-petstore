package pme123.petstore.shared

import enumeratum.{Enum, EnumEntry}
import julienrf.json.derived
import play.api.libs.json.OFormat

import scala.collection.immutable

sealed trait PetCategory extends EnumEntry {

  def subTitle: String

  def styleName:String = entryName.toLowerCase
  def identPrefix:String = entryName.take(3).toUpperCase
}

// see https://github.com/lloydmeta/enumeratum#usage
object PetCategory
  extends Enum[PetCategory] {

  val values: immutable.IndexedSeq[PetCategory] = findValues

  case object Fish extends PetCategory {
    def subTitle: String = "Saltwater, Freshwater"
  }

  case object Dogs extends PetCategory {
    def subTitle: String = "Various Breeds"
  }

  case object Mice extends PetCategory {
    def subTitle: String = "Various Breeds, Exotic Varieties"
  }

  case object Cats extends PetCategory {
    def subTitle: String = "Various Breeds"
  }

  case object Birds extends PetCategory {
    def subTitle: String = "Exotic Varieties"
  }

  implicit val jsonFormat: OFormat[PetCategory] = derived.oformat[PetCategory]()
}

