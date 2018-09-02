package pme123.petstore.server.control

import doobie.Fragment
import doobie.implicits._
import javax.inject.Inject
import pme123.petstore.server.control.services.DoobieDB
import pme123.petstore.shared.{Pet, PetCategory, PetProduct}
import pme123.petstore.shared.services._

import scala.concurrent.{ExecutionContext, Future}


class PetDBRepo @Inject()()
                         (implicit val ec:ExecutionContext)
  extends DoobieDB {

  def insertCategory(cat: PetCategory): Future[Int] =
    insert(
      sql"""insert into pet_categories (ident, name, sub_title)
             values (${cat.ident}, ${cat.name}, ${cat.subTitle})""")

  def selectCategories(where: Fragment = fr""): Future[List[PetCategory]] =
    select(
      (fr"select ident, name, sub_title from pet_categories" ++ where)
        .query[PetCategory]
    )

  def selectCategory(ident: String): Future[PetCategory] =
    selectCategories(fr"where ident = $ident")
      .map(_.head)

  def insertProduct(prod: PetProduct): Future[Int] =
    insert(
      sql"""insert into pet_products (ident, name, category, tags)
             values (${prod.ident}, ${prod.name}, ${prod.category.ident}, ${prod.tagsString})"""
    )

  def selectProducts(where: Fragment = fr""): Future[List[PetProduct]] =
    select(
      (fr"""select p.ident, p.name, p.tags, c.ident, c.name, c.sub_title
               from pet_products p
               left join pet_categories c
               on p.category = c.ident
         """ ++ where)
        .query[(String, String, String, PetCategory)]
        .map { case (ident, name, tags, cat) => PetProduct.apply(ident, name, cat, tags) }
    )

  def selectProduct(ident: String): Future[PetProduct] =
    selectProducts(fr"where p.ident = $ident")
      .map(_.head)

  def selectProductTags(): Future[Set[String]] =
    select(
      sql"""select p.tags
               from pet_products p
         """
        .query[String]
    ).map(_.toSet
      .flatMap((s: String) => s.splitToSet))

  def insertPet(pet: Pet): Future[Int] =
    insert(
      sql"""insert into pets (ident, descr, price, product, status, tags, photo_urls)
             values (${pet.ident}, ${pet.descr}, ${pet.price}, ${pet.product.ident}, ${pet.status.entryName}, ${pet.tagsString}, ${pet.photoUrlsString})"""
    )

  def selectPets(where: Fragment = fr""): Future[List[Pet]] =
    select((fr"""select p.ident, p.descr, p.price, p.status, p.tags, p.photo_urls,
                        pp.ident, pp.name, pp.tags,
                        c.ident, c.name, c.sub_title
                     from pets p
                     left join pet_products pp
                     on p.product = pp.ident
                     left join pet_categories c
                     on pp.category = c.ident
         """ ++ where)
      .query[(String, String, Double, String, String, String,
      String, String, String, PetCategory)]
      .map { case (pIdent, pDescr, pPrice, pStatus, pTags, pPhotoUrls, ident, name, tags, cat) =>
        Pet.create(pIdent, pDescr, pPrice, PetProduct.apply(ident, name, cat, tags), pStatus, pTags, pPhotoUrls)
      }
    )

  def selectPet(ident: String): Future[Pet] =
    selectPets(fr"where p.ident = $ident")
      .map(_.head)

  def selectPetTags(): Future[Set[String]] =
    select(
      sql"""select p.tags
               from pets p
         """
        .query[String]
    ).map(_.toSet
      .flatMap((s: String) => s.splitToSet))

}
