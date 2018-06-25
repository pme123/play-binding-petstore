package pme123.petstore.server.control


import javax.inject.{Inject, Singleton}
import pme123.petstore.shared.PetCategory.{Birds, Dogs}
import pme123.petstore.shared.{PetCategory, PetProduct, PetProducts}

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PetRepo @Inject()()
                       (implicit val ec: ExecutionContext) {

  def petProducts(petCategory: PetCategory): Future[PetProducts] =
    Future.successful(
      PetRepo.petProducts
        .filter(_.category == petCategory))
      .map(PetProducts(_))
}

object PetRepo {
  lazy val petProducts =
    List(
      PetProduct(nextProductIdent(Birds),
        "Amazon Parrot",
        Birds),
      PetProduct(nextProductIdent(Birds),
        "Finch",
        Birds),
      PetProduct(nextProductIdent(Dogs),
        "Bulldog",
        Dogs),
      PetProduct(nextProductIdent(Dogs),
        "Poodle",
        Dogs)
    )

  private val productIdentMap = mutable.Map[String, Int]()
  private val productIdentPrefix = ""

  private def nextProductIdent(category: PetCategory): String = {
    val prefix = category.identPrefix
    val nextIdent = productIdentMap.getOrElse(prefix, 0) + 1
    productIdentMap.update(prefix, nextIdent)
    f"$prefix-$nextIdent%03d"
  }

}

object NextIdent
