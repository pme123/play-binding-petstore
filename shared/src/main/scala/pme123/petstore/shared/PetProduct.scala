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
                       category: PetCategory,
                       tags: Set[String] = Set.empty
                     )
  extends Identifiable {

  val ident: String = productIdent

  val tagsString: String = tags.mkString(",")

  val link: String = s"#${PetProduct.name}/${category.ident}/$productIdent"

  def identPrefix: String = name.take(3).toUpperCase

}

object PetProduct {

  val name: String = "product"

  def apply(
             productIdent: String,
             name: String,
             category: PetCategory,
             tags: String
           ): PetProduct = new PetProduct(productIdent, name, category, tags.splitToSet)

  implicit val jsonFormat: OFormat[PetProduct] = derived.oformat[PetProduct]()

}
