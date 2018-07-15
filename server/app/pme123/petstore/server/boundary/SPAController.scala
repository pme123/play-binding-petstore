package pme123.petstore.server.boundary

import play.Environment
import play.api.i18n.I18nSupport
import play.api.mvc._
import pme123.petstore.server.boundary.services.Secured
import pme123.petstore.server.control.PetConfiguration
import pme123.petstore.server.entity.PageConfig
import pme123.petstore.shared.services.AccessControl

import scala.concurrent.Future

abstract class SPAController(bpfComps: SPAComponents)
  extends AbstractController(bpfComps.cc)
    with I18nSupport
    with Secured {

  lazy val env: Environment = bpfComps.env
  lazy val config: PetConfiguration = bpfComps.config
  lazy val cc: ControllerComponents = bpfComps.cc
  lazy val accessControl: AccessControl = bpfComps.accessControl

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