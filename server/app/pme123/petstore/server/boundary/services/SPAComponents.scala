package pme123.petstore.server.boundary.services

import com.mohiva.play.silhouette.api.Silhouette
import controllers.AssetsFinder
import javax.inject.Inject
import play.Environment
import play.api.mvc._
import pme123.petstore.server.boundary.IdentityApi
import pme123.petstore.server.control.PetConfiguration
import pme123.petstore.server.control.auth.DefaultEnv

import scala.concurrent.ExecutionContext

class SPAComponents @Inject()(val assetsFinder: AssetsFinder,
                              val config: PetConfiguration,
                              val env: Environment,
                              val cc: ControllerComponents,
                              val identityApi: IdentityApi,
                              val silhouette: Silhouette[DefaultEnv]
                             )(implicit ec: ExecutionContext) {


}
