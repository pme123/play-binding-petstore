package pme123.petstore.server.control


import javax.inject.{Inject, Singleton}
import pme123.petstore.shared.PetCategory.{Birds, Dogs}
import pme123.petstore.shared._

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PetRepo @Inject()()
                       (implicit val ec: ExecutionContext) {

  def petCategories(): Future[PetCategories] =
    Future.successful(
      PetCategories(
        PetCategory.values.toList
      )
    )

  def petProducts(petCategory: PetCategory): Future[PetProducts] =
    Future.successful(
      PetProductsRepo.petProducts
        .filter(_.category == petCategory))
      .map(PetProducts(petCategory, _))


  def pets(productIdent: String): Future[Pets] = {
    Future {
      val prod = PetProductsRepo.petProduct(productIdent)
      Pets(prod, PetsRepo.pets
        .filter(_.product == prod)
      )
    }
  }
}

object PetsRepo {

  import PetProductsRepo._

  lazy val pets =
    List(
      Pet(nextItemIdent(parrot),
        "Adult Male Amazon Parrot",
        193.5, parrot),
      Pet(nextItemIdent(finch),
        "Adult Male Finch",
        34.95, finch),
      Pet(nextItemIdent(bulldog),
        "Male Adult Bulldog",
        342, bulldog, tags = Set("male")),
      Pet(nextItemIdent(bulldog),
        "Female Puppy Bulldog",
        367, bulldog, tags = Set("female"))
    )

  private val petIdentMap = mutable.Map[String, Int]()

  private def nextItemIdent(petProduct: PetProduct): String = {
    val prefix = petProduct.identPrefix
    val nextIdent = petIdentMap.getOrElse(prefix, 0) + 1
    petIdentMap.update(prefix, nextIdent)
    f"$prefix-$nextIdent%03d"
  }

}

object PetProductsRepo {

  lazy val petProducts =
    List(
      parrot,
      finch,
      bulldog,
      poodle
    )

  def petProduct(productIdent: String): PetProduct =
    PetProductsRepo.petProducts
      .find(_.productIdent == productIdent)
      .get

  lazy val parrot = PetProduct(nextProductIdent(Birds),
    "Amazon Parrot",
    Birds, Set("tropical", "talks"))
  lazy val finch = PetProduct(nextProductIdent(Birds),
    "Finch",
    Birds)
  lazy val bulldog = PetProduct(nextProductIdent(Dogs),
    "Bulldog",
    Dogs)
  lazy val poodle = PetProduct(nextProductIdent(Dogs),
    "Poodle",
    Dogs)

  private val productIdentMap = mutable.Map[String, Int]()

  private def nextProductIdent(category: PetCategory): String = {
    val prefix = category.identPrefix
    val nextIdent = productIdentMap.getOrElse(prefix, 0) + 1
    productIdentMap.update(prefix, nextIdent)
    f"$prefix-$nextIdent%03d"
  }

}
