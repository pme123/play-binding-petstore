package pme123.petstore.server.control


import doobie.implicits._
import doobie.util.fragment.Fragment
import javax.inject.{Inject, Singleton}
import pme123.petstore.server.entity.ImportWorkbook.workbook
import pme123.petstore.shared.{Pet, PetCategory, PetProduct, _}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

@Singleton
class PetRepo @Inject()()
                       (implicit val ec: ExecutionContext)
  extends PetDBRepo {

  def petCategories(): Future[PetCategories] =
    Future(
      PetCategories(selectCategories())
    )

  def petCategory(petCategory: String): PetCategory =
    selectCategory(petCategory)

  def productTags(): Future[Set[String]] =
    Future(
      selectProductTags()
    )

  def petTags(): Future[Set[String]] =
    Future(
      selectPetTags()
    )

  def petProducts(petCategory: PetCategory): Future[PetProducts] =
    Future(
      PetProducts(petCategory, selectProducts(fr"where p.category = ${petCategory.ident}"))
    )

  def pets(productIdent: String): Future[Pets] = {
    Future {
      Pets(selectProduct(productIdent), selectPets(fr"where p.product = $productIdent")
      )
    }
  }

  def pet(petIdent: String): Future[Pet] =
    Future {
      selectPet(petIdent)
    }

  def filter(petFilter: PetFilter): Future[Seq[Pet]] =
    Future {
      if (petFilter.nonEmpty) {
        val filters = Seq(
          petFilter.petDescr.map(f => fr"p.descr like '%$f%'")
        )
        val frs: Fragment = filters
          .filter(_.nonEmpty)
          .map(_.get)
          .foldLeft(fr"where ")((a, b) => a ++ b)

        selectPets()
        //PetsRepo.filter(petFilter)
      } else
        selectPets() // return one page of animals
    }
}

object PetsRepo {

  lazy val pets: Map[String, Pet] =
    workbook.pets.map { pets =>
      pets.collect {
        case Success(p) => (p.ident, p)
      }.toMap
    }.get

  lazy val tags: Set[String] = pets.values.toSet.flatMap((p: Pet) => p.tags)

  def filter(petFilter: PetFilter): Seq[Pet] =
    pets.values.toSeq
      .filter(p => filterText(petFilter.petDescr, p.descr))
      .filter(p => filterText(petFilter.product, p.product.name))
      .filter(p => filterTextSeq(petFilter.petTags, p.tags))
      .filter(p => filterTextSeq(petFilter.productTags, p.product.tags))
      .filter(p => petFilter.categories.isEmpty || petFilter.categories.contains(p.product.category.ident))

  def pet(petIdent: String): Pet =
    pets(petIdent)

  private def filterText(filter: Option[String], text: String): Boolean =
    filter.isEmpty || text.toLowerCase.contains(filter.get.toLowerCase())

  private def filterTextSeq(filter: Seq[String], textSet: Set[String]): Boolean =
    filter.isEmpty || filter.exists(textSet.contains)
}

object PetProductsRepo {

  lazy val products: Map[String, PetProduct] =
    workbook.petProducts.map { products =>
      products.collect {
        case Success(pp) => (pp.ident, pp)
      }.toMap
    }.get

  lazy val tags: Set[String] = products.values.toSet.flatMap((p: PetProduct) => p.tags)

}


object PetCategoriesRepo {

  lazy val categories: Map[String, PetCategory] =
    workbook.categories.map { categories =>
      categories.collect {
        case Success(pc) => (pc.ident, pc)
      }.toMap
    }.get
}
