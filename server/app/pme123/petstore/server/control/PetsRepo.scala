package pme123.petstore.server.control


import javax.inject.{Inject, Singleton}
import pme123.petstore.server.entity.ImportWorkbook.workbook
import pme123.petstore.shared._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

@Singleton
class PetRepo @Inject()()
                       (implicit val ec: ExecutionContext) {

  def petCategories(): Future[PetCategories] =
    Future.successful(
      PetCategories(PetCategoriesRepo.categories.values.toList)
    )

  def petCategory(petCategory: String): PetCategory =
    PetCategoriesRepo.categories(petCategory)

  def productTags(): Future[Set[String]] =
    Future.successful(
      PetProductsRepo.tags
    )

  def petTags(): Future[Set[String]] =
    Future.successful(
      PetsRepo.tags
    )

  def petProducts(petCategory: PetCategory): Future[PetProducts] =
    Future.successful(
      PetProductsRepo.products.values.toList
        .filter(_.category == petCategory)
        .sortBy(_.name))
      .map(PetProducts(petCategory, _))

  def pets(productIdent: String): Future[Pets] = {
    Future {
      val prod = PetProductsRepo.products(productIdent)
      Pets(prod, PetsRepo.pets.values.toList
        .filter(_.product == prod)
      )
    }
  }

  def filter(petFilter: PetFilter): Future[Seq[Pet]] = {
    Future {
      if (petFilter.nonEmpty)
        PetsRepo.filter(petFilter)
      else
        Nil
    }
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
