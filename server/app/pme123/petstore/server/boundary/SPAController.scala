package pme123.petstore.server.boundary

import play.Environment
import play.api.i18n.I18nSupport
import play.api.mvc._
import pme123.petstore.server.boundary.services.Secured
import pme123.petstore.server.control.PetConfiguration
import pme123.petstore.server.entity.PageConfig
import pme123.petstore.shared.services.{AccessControl, Logging}

import scala.concurrent.Future

abstract class SPAController(spaComps: SPAComponents)
  extends AbstractController(spaComps.cc)
    with I18nSupport
    with Secured
    with Logging {

  lazy val env: Environment = spaComps.env
  lazy val config: PetConfiguration = spaComps.config
  lazy val cc: ControllerComponents = spaComps.cc
  lazy val accessControl: AccessControl = spaComps.accessControl

  def pageConfig(maybeUsername: Option[String]): Future[PageConfig] =
    Future.successful(PageConfig(context, env.isDev))


  private def context: String = {
    val context = if (config.httpContext.length > 1)
      config.httpContext
    else
      ""
    context
  }


}