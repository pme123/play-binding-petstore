package pme123.petstore.server.control


import javax.inject.{Inject, Singleton}
import pme123.petstore.shared.PetCategory.{Birds, Dogs, Fish}
import pme123.petstore.shared._

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PetRepo @Inject()()
                       (implicit val ec: ExecutionContext) {

  def productTags(): Future[Seq[String]] =
    Future.successful(
      PetProductsRepo.tags
    )

  def petTags(): Future[Seq[String]] =
    Future.successful(
      PetsRepo.tags
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

  def filter(petFilter: PetFilter): Future[Seq[Pet]] = {
    Future {
      PetsRepo.filter(petFilter)
    }
  }
}

object PetsRepo {

  import PetProductsRepo._

  lazy val tags = Seq("male", "female")

  lazy val pets =
    List(
      Pet(nextItemIdent(parrot),
        "Adult Male Amazon Parrot",
        193.5, parrot, tags = Set("male")),
      Pet(nextItemIdent(finch),
        "Adult Male Finch",
        34.95, finch, tags = Set("male")),
      Pet(nextItemIdent(bulldog),
        "Male Adult Bulldog",
        342, bulldog, tags = Set("male")),
      Pet(nextItemIdent(bulldog),
        "Female Puppy Bulldog",
        367, bulldog, tags = Set("female")),
      Pet(nextItemIdent(shark),
        "Crazy and Dangerous Tiger Shark!",
        1345, shark, tags = Set("female"))
    )

  private val petIdentMap = mutable.Map[String, Int]()

  private def nextItemIdent(petProduct: PetProduct): String = {
    val prefix = petProduct.identPrefix
    val nextIdent = petIdentMap.getOrElse(prefix, 0) + 1
    petIdentMap.update(prefix, nextIdent)
    f"$prefix-$nextIdent%03d"
  }

  def filter(petFilter: PetFilter): Seq[Pet] =
    pets.filter(p => filterText(petFilter.petDescr, p.descr))
      .filter(p => filterText(petFilter.product, p.product.name))
      .filter(p => petFilter.categories.isEmpty || petFilter.categories.contains(p.product.category.entryName))
      .filter(p => petFilter.petTags.isEmpty || petFilter.petTags.exists(p.tags.contains))
      .filter(p => petFilter.productTags.isEmpty || petFilter.productTags.exists(p.product.tags.contains))

  private def filterText(filter: Option[String], text: String): Boolean =
    filter.isEmpty || text.toLowerCase.contains(filter.get.toLowerCase())


}

object PetProductsRepo {

  lazy val tags = Seq("tropical", "talks")

  lazy val petProducts =
    List(
      parrot,
      finch,
      bulldog,
      poodle,
      shark
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
  lazy val shark = PetProduct(nextProductIdent(Fish),
    "Tiger Shark",
    Fish, Set("tropical"))

  private val productIdentMap = mutable.Map[String, Int]()

  private def nextProductIdent(category: PetCategory): String = {
    val prefix = category.identPrefix
    val nextIdent = productIdentMap.getOrElse(prefix, 0) + 1
    productIdentMap.update(prefix, nextIdent)
    f"$prefix-$nextIdent%03d"
  }

}
