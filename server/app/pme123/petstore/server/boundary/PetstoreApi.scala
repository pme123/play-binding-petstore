package pme123.petstore.server.boundary

import cats.data.NonEmptyList
import doobie._
import doobie.implicits._
import doobie.util.fragment.Fragment
import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.boundary.services.{SPAComponents, SPAController}
import pme123.petstore.server.control.PetDBRepo
import pme123.petstore.shared._

import scala.concurrent.{ExecutionContext, Future}

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class PetstoreApi @Inject()(petDBRepo: PetDBRepo,
                            val spaComps: SPAComponents)
                           (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  def productTags(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    petDBRepo.selectProductTags()
      .map(tags =>
        Ok(Json.toJson(tags)).as(JSON)
      )
  }

  def petTags(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    petDBRepo.selectPetTags()
      .map(tags =>
        Ok(Json.toJson(tags)).as(JSON)
      )
  }

  def petCategories(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    petDBRepo.selectCategories()
      .map(categories =>
        Ok(Json.toJson(PetCategories(categories))).as(JSON)
      )
  }

  def petProducts(petCategory: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    (for {
      cat <- petDBRepo.selectCategory(petCategory)
      products <- petDBRepo.selectProducts(fr"where p.category = $petCategory")
    } yield PetProducts(cat, products))
      .map(products =>
        Ok(Json.toJson(products)).as(JSON)
      )
  }

  def pets(productIdent: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    (for {prod <- petDBRepo.selectProduct(productIdent)
          pets <- petDBRepo.selectPets(fr"where p.product = $productIdent")
    } yield Pets(prod, pets)
      ).map(products =>
      Ok(Json.toJson(products)).as(JSON)
    )
  }

  def pet(petIdent: String): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    petDBRepo.selectPet(petIdent)
      .map(pet =>
        Ok(Json.toJson(pet)).as(JSON)
      )
  }

  def filter(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    request.body.asText.map { body =>
      Json.parse(body).validate[PetFilter]
        .map(filter)
        .getOrElse(Future.successful(Seq[Pet]()))
        .map(pets =>
          Ok(Json.toJson(pets)).as(JSON)
        )
    }.getOrElse(Future.successful(Ok(JsArray()).as(JSON)))
  }

  private def filter(petFilter: PetFilter): Future[Seq[Pet]] =
    if (petFilter.nonEmpty) {
      val filters = Seq(
        petFilter.petDescr.map(f => s"%$f%").map(f => fr"lower(p.descr) like $f"),
        petFilter.product.map(f => s"%$f%").map(f => fr"lower(pp.name) like $f"),
        inFilter(fr"pp.category", petFilter.categories),
        tagsFilter(fr"pp.tags", petFilter.productTags),
        tagsFilter(fr"p.tags", petFilter.petTags)
      )
      val frs = filters
        .filter(_.nonEmpty)
        .map(_.get) match {
        case Nil => fr""
        case x :: Nil => fr"where" ++ x
        case x :: tail => tail.foldLeft(fr"where" ++ x)((a, b) => a ++ fr"AND" ++ b)
      }
      petDBRepo.selectPets(frs)
    } else
      petDBRepo.selectPets() // return one page of animals

  private def inFilter(field: Fragment, ids: Seq[String]): Option[Fragment] =
    ids match {
      case Nil => None
      case x :: tail => Some(Fragments.in(field, NonEmptyList(x, tail)))
    }

  private def tagsFilter(field: Fragment, tags: Seq[String]): Option[Fragment] =
    tags.map(t=>s"%$t%") match {
      case Nil => None
      case x :: tail =>
        Some(tail.foldLeft(field ++ fr"like $x")((a, b) => a ++ fr"OR" ++ field ++ fr"like $b"))
    }
}
