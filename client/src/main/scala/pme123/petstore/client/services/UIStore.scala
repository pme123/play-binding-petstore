package pme123.petstore.client.services

import com.thoughtworks.binding.Binding.{Var, Vars}
import pme123.petstore.shared.services._

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

  def changeComments(comments: Comments): Comments = {
    info(s"UIStore: changeComments for ${comments.user}")
    uiState.comments.value.clear()
    uiState.comments.value ++= comments.comments
    comments
  }

  case class UIState(
                      loggedInUser: Var[LoggedInUser] = Var(LoggedInUser()),
                      comments: Vars[Comment] = Vars(),
                      webContext: Var[String] = Var("")
                    )

}

case class LoggedInUser(maybeUser: Option[User] = None) {

  val isDefined: Boolean = maybeUser.isDefined

  val isAdmin: Boolean = maybeUser.isDefined && maybeUser.get.isAdmin
  val isManager: Boolean = maybeUser.isDefined && maybeUser.get.isManager
  val isCustomer: Boolean = maybeUser.isEmpty || maybeUser.get.isCustomer

  val avatar: String = {
    val avatar = maybeUser.map(_.avatar).getOrElse("anonymous.png")
    s"/images/users/$avatar"
  }
  val fullName: String = maybeUser.map(_.fullName).getOrElse("Anonymous")

  val username: String = maybeUser.map(_.id).getOrElse("anonymous")
}