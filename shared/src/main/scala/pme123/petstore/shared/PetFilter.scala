package pme123.petstore.shared

import julienrf.json.derived
import play.api.libs.json.OFormat

case class PetFilter(petDescr: Option[String] = None,
                     product: Option[String] = None,
                     categories: Seq[PetFilter.Category] = Nil,
                     petTags: Seq[PetFilter.PetTag] = Nil,
                     productTags: Seq[PetFilter.ProductTag] = Nil) {

  def withPetDescr(petDescr: Option[String]): PetFilter = {
    copy(petDescr = petDescr)
  }

  def withProduct(product: Option[String]): PetFilter = {
    copy(product = product)
  }

  def withCategories(categories: String): PetFilter = {
    if (categories.nonEmpty) {
      copy(categories = categories.split(","))
    } else copy(categories = Nil)
  }

  def withPetTags(tags: String): PetFilter = {
    if (tags.nonEmpty) {
      copy(petTags = tags.split(","))
    } else copy(petTags = Nil)
  }

  def withProductTags(tags: String): PetFilter = {
    if (tags.nonEmpty) {
      copy(productTags = tags.split(","))
    } else copy(productTags = Nil)
  }

}

object PetFilter {

  type Category = String
  type PetTag = String
  type ProductTag = String

  implicit val jsonFormat: OFormat[PetFilter] = derived.oformat[PetFilter]()


}
