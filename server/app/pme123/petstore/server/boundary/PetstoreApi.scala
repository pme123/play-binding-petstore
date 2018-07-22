package pme123.petstore.server.boundary

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.control.PetRepo
import pme123.petstore.shared.{Pet, PetFilter}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class PetstoreApi @Inject()(petRepo: PetRepo,
                            val spaComps: SPAComponents)
                           (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  def productTags(): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    petRepo.productTags()
      .map(tags =>
        Ok(Json.toJson(tags)).as(JSON)
      )
  }

  def petTags(): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    petRepo.petTags()
      .map(tags =>
        Ok(Json.toJson(tags)).as(JSON)
      )
  }

  def petCategories(): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    petRepo.petCategories()
      .map(products =>
        Ok(Json.toJson(products)).as(JSON)
      )
  }

  def petProducts(petCategory: String): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    petRepo.petProducts(petRepo.petCategory(petCategory))
      .map(products =>
        Ok(Json.toJson(products)).as(JSON)
      )
  }

  def pets(productIdent: String): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    petRepo.pets(productIdent)
      .map(products =>
        Ok(Json.toJson(products)).as(JSON)
      )
  }

  def filter(): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    request.body.asText.map { body =>
      Json.parse(body).validate[PetFilter]
        .map(petRepo.filter)
        .getOrElse(Future.successful(Seq[Pet]()))
        .map(pets =>
          Ok(Json.toJson(pets)).as(JSON)
        )
    }.getOrElse(Future.successful(Ok(JsArray()).as(JSON)))
  }

}
