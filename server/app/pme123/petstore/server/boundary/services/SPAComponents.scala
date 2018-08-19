package pme123.petstore.server.boundary.services

import com.mohiva.play.silhouette.api.Silhouette
import controllers.AssetsFinder
import javax.inject.Inject
import play.Environment
import play.api.mvc._
import pme123.petstore.server.control.PetConfiguration
import pme123.petstore.server.control.auth.{DefaultEnv, IdentityService}

import scala.concurrent.ExecutionContext

class SPAComponents @Inject()(val assetsFinder: AssetsFinder,
                              val config: PetConfiguration,
                              val env: Environment,
                              val cc: ControllerComponents,
                              val identityApi: IdentityService,
                              val silhouette: Silhouette[DefaultEnv]
                             )(implicit ec: ExecutionContext) {


}
