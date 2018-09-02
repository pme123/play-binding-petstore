package pme123.petstore.shared

import enumeratum.{Enum, EnumEntry}
import julienrf.json.derived
import play.api.libs.json.OFormat

import scala.collection.immutable
import pme123.petstore.shared.services._

case class Pets(
                 petProduct: PetProduct,
                 pets: List[Pet] = Nil
               )

object Pets {
  implicit val jsonFormat: OFormat[Pets] = derived.oformat[Pets]()

}

case class Pet(
                itemIdent: String,
                descr: String,
                price: Double,
                product: PetProduct,
                status: PetStatus = PetStatus.Available,
                tags: Set[String] = Set.empty,
                photoUrls: Set[String] = Set.empty
              )
  extends Identifiable {

  val link = s"#${Pet.name}/${product.category.ident}/${product.productIdent}/$itemIdent"

  val firstPhotoUrl: String = s"images/catalog/${photoUrls.headOption.getOrElse("noimage.png")}"

  val ident: String = itemIdent

  val tagsString: String = tags.mkString(",")
  val photoUrlsString: String = photoUrls.mkString(",")

  val priceAsStr = f"$$ $price%.2f"
}

object Pet {

  def name: String = "pet"

  def create(
             itemIdent: String,
             descr: String,
             price: Double,
             product: PetProduct,
             status: String,
             tags: String,
             photoUrls: String
           ): Pet = new Pet(itemIdent, descr, price, product, PetStatus.withNameInsensitive(status), tags.splitToSet, photoUrls.splitToSet)

  implicit val jsonFormat: OFormat[Pet] = derived.oformat[Pet]()

}

sealed trait PetStatus extends EnumEntry

// see https://github.com/lloydmeta/enumeratum#usage
object PetStatus
  extends Enum[PetStatus] {

  val values: immutable.IndexedSeq[PetStatus] = findValues

  case object Available extends PetStatus

  case object Pending extends PetStatus

  case object Adopted extends PetStatus

  implicit val jsonFormat: OFormat[PetStatus] = derived.oformat[PetStatus]()
}

