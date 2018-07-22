package pme123.petstore.server.boundary.services

import javax.inject._
import play.api.libs.json._
import play.api.mvc._
import pme123.petstore.server.control.PetRepo
import pme123.petstore.server.control.services.UserRepo
import pme123.petstore.shared.services.AuthUser
import pme123.petstore.shared.{Pet, PetFilter}

import scala.concurrent.{ExecutionContext, Future}

/**
  * This class creates the actions and the websocket needed.
  * Original see here: https://github.com/playframework/play-scala-websocket-example
  */
@Singleton
class UserApi @Inject()(petRepo: PetRepo,
                        val spaComps: SPAComponents)
                       (implicit val ec: ExecutionContext)
  extends SPAController(spaComps) {

  private val demoCustomer = "demoCustomer"
  private val demoManager = "demoManager"
  private val demoAdmin = "demoAdmin"

  val authUsers = Map(
    demoCustomer -> AuthUser(demoCustomer),
    demoManager -> AuthUser(demoManager),
    demoAdmin -> AuthUser(demoAdmin)
  )

  def loggedInUser(): Action[AnyContent] = AuthenticatedAction.async { implicit request: Request[AnyContent] =>
    val user = extractUser.map(UserRepo.userFor)

    Future.successful(
      Ok(Json.toJson(user)).as(JSON)
    )
  }
}
