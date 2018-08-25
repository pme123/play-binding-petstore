package pme123.petstore.server.control

import cats.effect.IO
import doobie.Fragment
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import pme123.petstore.shared._
import pme123.petstore.shared.{Pet, PetCategory, PetProduct}

trait PetDB {

  protected val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost/postgres",
    "postgres",
    "3sf2reRer"
  )
/*
H2
  protected val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "org.h2.Driver",
    "jdbc:h2:~/dev/servers/h2",
    "sa",
    ""
  )
  */
}

trait PetDBRepo extends PetDB {

  def insertCategory(cat: PetCategory): Int =
    insert(
      sql"""insert into pet_category (ident, name, sub_title)
             values (${cat.ident}, ${cat.name}, ${cat.subTitle})""")

  def selectCategories(where: Fragment = fr""): List[PetCategory] =
    select(
      (fr"select ident, name, sub_title from pet_category" ++ where)
        .query[PetCategory]
    )

  def selectCategory(ident: String): PetCategory =
    selectCategories(fr"where ident = $ident")
      .head

  def insertProduct(prod: PetProduct): Int =
    insert(
      sql"""insert into pet_product (ident, name, category, tags)
             values (${prod.ident}, ${prod.name}, ${prod.category.ident}, ${prod.tagsString})"""
    )

  def selectProducts(where: Fragment = fr""): List[PetProduct] =
    select(
      (fr"""select p.ident, p.name, p.tags, c.ident, c.name, c.sub_title
               from pet_product p
               left join pet_category c
               on p.category = c.ident
         """ ++ where)
        .query[(String, String, String, PetCategory)]
        .map { case (ident, name, tags, cat) => PetProduct.apply(ident, name, cat, tags) }
    )

  def selectProduct(ident: String): PetProduct =
    selectProducts(fr"where p.ident = $ident")
      .head

  def selectProductTags(): Set[String] =
    select(
      sql"""select p.tags
               from pet_product p
         """
        .query[String]
    ).toSet
      .flatMap((s:String) => s.splitToSet)

  def insertPet(pet: Pet): Int =
    insert(
      sql"""insert into pet (ident, descr, price, product, status, tags, photo_urls)
             values (${pet.ident}, ${pet.descr}, ${pet.price}, ${pet.product.ident}, ${pet.status.entryName}, ${pet.tagsString}, ${pet.photoUrlsString})"""
    )

  def selectPets(where: Fragment = fr""): List[Pet] =
    select((fr"""select p.ident, p.descr, p.price, p.status, p.tags, p.photo_urls,
                        pp.ident, pp.name, pp.tags,
                        c.ident, c.name, c.sub_title
                     from pet p
                     left join pet_product pp
                     on p.product = pp.ident
                     left join pet_category c
                     on pp.category = c.ident
         """ ++ where)
      .query[(String, String, Double, String, String, String,
      String, String, String, PetCategory)]
      .map { case (pIdent, pDescr, pPrice, pStatus, pTags, pPhotoUrls, ident, name, tags, cat) =>
        Pet.create(pIdent, pDescr, pPrice, PetProduct.apply(ident, name, cat, tags), pStatus, pTags, pPhotoUrls)
      }
    )

  def selectPet(ident: String): Pet =
    selectPets(fr"where p.ident = $ident")
      .head

  def selectPetTags(): Set[String] =
    select(
      sql"""select p.tags
               from pet p
         """
        .query[String]
    ).toSet
      .flatMap((s:String) => s.splitToSet)

  private def insert(sql: Fragment) =
    sql
      .update
      .run
      .transact(xa)
      .unsafeRunSync

  private def select[T](query: Query0[T]): List[T] =
    query
      .stream
      .transact(xa)
      .take(50)
      .compile
      .toList
      .unsafeRunSync

}
