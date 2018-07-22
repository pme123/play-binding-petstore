package pme123.petstore.client.services

import com.thoughtworks.binding.Binding.Var
import pme123.petstore.shared.services.{Logging, User}

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
    uiState.loggedInUser.value = Some(loggedInUser)
    loggedInUser
  }

  case class UIState(
                      loggedInUser: Var[Option[User]] = Var(None),
                        webContext: Var[String] = Var("")
                    )


}