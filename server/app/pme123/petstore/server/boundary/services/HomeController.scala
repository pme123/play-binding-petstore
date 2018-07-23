package pme123.petstore.server.boundary.services

import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class HomeController @Inject()(template: views.html.index
                               , val spaComps: SPAComponents)
                              (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  def index(): Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    // uses the AssetsFinder API
    pageConfig(None)
      .map(pc => Ok(template(pc)))
  }

}

