package pme123.petstore.shared

import julienrf.json.derived
import play.api.libs.json.OFormat

case class PetCategories(
                          categories: List[PetCategory] = Nil
                        ) {

}

object PetCategories {
  implicit val jsonFormat: OFormat[PetCategories] = derived.oformat[PetCategories]()

}

case class PetCategory(ident: String, name: String, subTitle: String)
  extends Identifiable {

  def styleName: String = ident

  def identPrefix: String = ident.take(3).toUpperCase

  val link: String = s"#${PetCategory.name}/$ident"

}

object PetCategory {

  def name: String = "category"

  implicit val jsonFormat: OFormat[PetCategory] = derived.oformat[PetCategory]()
}

