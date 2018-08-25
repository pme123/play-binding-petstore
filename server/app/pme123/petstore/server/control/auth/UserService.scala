package pme123.petstore.server.control.auth

import com.mohiva.play.silhouette
import com.mohiva.play.silhouette.api.LoginInfo
import javax.inject.Inject
import pme123.petstore.server.control.services.UserRepo
import pme123.petstore.server.entity.AuthUser

import scala.concurrent.{ExecutionContext, Future}

class UserService @Inject()()
                           (implicit ec: ExecutionContext)
  extends silhouette.api.services.IdentityService[AuthUser] {

  val providerKey = "petstore"

  override def retrieve(loginInfo: LoginInfo): Future[Option[AuthUser]] =
    if (loginInfo.providerKey == providerKey) {
      Future.successful(Some(UserRepo.authUser(loginInfo.providerID)))
    }
    else
      Future.successful(None)
}

