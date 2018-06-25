package pme123.petstore.server.boundary

import controllers.AssetsFinder
import javax.inject.Inject
import play.Environment
import play.api.mvc._
import pme123.petstore.server.control.PetConfiguration
import pme123.petstore.shared.services.AccessControl

import scala.concurrent.{ExecutionContext, Future}

class SPAComponents @Inject()(val assetsFinder: AssetsFinder,
                              val config: PetConfiguration,
                              val env: Environment,
                              val cc: ControllerComponents,
                              val accessControl: AccessControl
                             )(implicit ec: ExecutionContext) {



}
