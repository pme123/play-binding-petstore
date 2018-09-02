package pme123.petstore.server.control.auth

import com.mohiva.play.silhouette
import com.mohiva.play.silhouette.api.LoginInfo
import javax.inject.Inject
import pme123.petstore.server.control.services.UserDBRepo
import pme123.petstore.server.entity.AuthUser

import scala.concurrent.{ExecutionContext, Future}

class UserService @Inject()(userDBRepo: UserDBRepo)
                           (implicit ec: ExecutionContext)
  extends silhouette.api.services.IdentityService[AuthUser] {

  val providerKey = "petstore"

  override def retrieve(loginInfo: LoginInfo): Future[Option[AuthUser]] =
    if (loginInfo.providerKey == providerKey) {
      userDBRepo.selectAuthUser(loginInfo.providerID)
        .map(Some(_))
    }
    else
      Future.successful(None)
}

