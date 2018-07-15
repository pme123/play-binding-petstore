package pme123.petstore.shared

import julienrf.json.derived
import play.api.libs.json.OFormat

case class PetProducts(
                        category: PetCategory,
                        products: List[PetProduct] = Nil
                      ) {

}

object PetProducts {
  implicit val jsonFormat: OFormat[PetProducts] = derived.oformat[PetProducts]()

}


case class PetProduct(
                       productIdent: String,
                       name: String,
                       category: PetCategory = PetCategory.Dogs,
                       tags: Set[String] = Set.empty
                     ) {
  def identPrefix:String = name.take(3).toUpperCase

}

object PetProduct {
  implicit val jsonFormat: OFormat[PetProduct] = derived.oformat[PetProduct]()

}
