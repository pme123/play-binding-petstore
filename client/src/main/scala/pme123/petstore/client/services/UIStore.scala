package pme123.petstore.client.services

import com.thoughtworks.binding.Binding.Var
import pme123.petstore.shared.services.{AuthUser, Logging, User}

import scala.language.implicitConversions

object UIStore extends Logging {

  val uiState = UIState()

  def changeWebContext(webContext: String): String = {
    info(s"UIStore: changeWebContext $webContext")
    uiState.webContext.value = webContext
    webContext
  }

  def changeLoggedInUser(loggedInUser: User): User = {
    info(s"UIStore: changeLoggedInUser $loggedInUser")
    uiState.loggedInUser.value = LoggedInUser(Some(loggedInUser))
    loggedInUser
  }

  case class UIState(
                      loggedInUser: Var[LoggedInUser] = Var(LoggedInUser()),
                        webContext: Var[String] = Var("")
                    )

}

case class LoggedInUser(maybeUser: Option[User] = None) {

  val isDefined: Boolean = maybeUser.isDefined

  val isAdmin: Boolean = maybeUser.isDefined && maybeUser.get.authUser.groups.contains(AuthUser.adminGroup)
  val isManager: Boolean = maybeUser.isDefined && maybeUser.get.authUser.groups.contains(AuthUser.managerGroup)
  val isCustomer: Boolean = maybeUser.isEmpty || maybeUser.get.authUser.groups.contains(AuthUser.customerGroup)

  val avatar: String = {
    val avatar = maybeUser.map(_.avatar).getOrElse("anonymous.png")
    s"/images/users/$avatar"
  }
  val fullName: String = maybeUser.map(_.fullName).getOrElse("Anonymous")
}