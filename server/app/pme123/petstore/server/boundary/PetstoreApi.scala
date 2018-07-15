package pme123.petstore.server.boundary

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.control.PetRepo
import pme123.petstore.shared.PetCategory

import scala.concurrent.ExecutionContext

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class PetstoreApi @Inject()(petRepo: PetRepo,
                            val spaComps: SPAComponents)
                           (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  def petCategories(): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    petRepo.petCategories()
      .map(categories =>
        Ok(Json.toJson(categories)).as(JSON)
      )
  }

  def petProducts(petCategory: String): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    petRepo.petProducts(PetCategory.withNameInsensitive(petCategory))
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


}
