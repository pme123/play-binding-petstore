package pme123.petstore.server.boundary.services

import com.mohiva.play.silhouette.api.Silhouette
import play.Environment
import play.api.i18n.I18nSupport
import play.api.mvc._
import pme123.petstore.server.control.PetConfiguration
import pme123.petstore.server.control.auth.{DefaultEnv, IdentityService, SecuredController}
import pme123.petstore.server.entity.{AuthUser, PageConfig}
import pme123.petstore.shared.services.Logging

import scala.concurrent.Future

abstract class SPAController(spaComps: SPAComponents)
  extends AbstractController(spaComps.cc)
    with SecuredController[DefaultEnv]
    with I18nSupport
    with Logging {

  lazy val env: Environment = spaComps.env
  lazy val config: PetConfiguration = spaComps.config
  lazy val cc: ControllerComponents = spaComps.cc
  lazy val identityApi: IdentityService = spaComps.identityApi
  def silhouette: Silhouette[DefaultEnv] = spaComps.silhouette

  def pageConfig(maybeUser: Option[AuthUser]): Future[PageConfig] =
    Future.successful(PageConfig(context, env.isDev))


  private def context: String = {
    val context = if (config.httpContext.length > 1)
      config.httpContext
    else
      ""
    context
  }


}