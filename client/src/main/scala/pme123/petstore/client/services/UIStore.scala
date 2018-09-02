package pme123.petstore.client.services

import com.thoughtworks.binding.Binding.{Var, Vars}
import pme123.petstore.shared.services.{LoggedInUser, _}

import scala.language.implicitConversions

object UIStore extends Logging {

  val uiState = UIState()

  def changeWebContext(webContext: String): String = {
    info(s"UIStore: changeWebContext $webContext")
    uiState.webContext.value = webContext
    webContext
  }

  def changeLoggedInUser(loggedInUser: LoggedInUser): LoggedInUser = {
    info(s"UIStore: changeLoggedInUser $loggedInUser")
    uiState.loggedInUser.value = loggedInUser
    loggedInUser
  }

  def changeNewComment(text: String): String = {
    info(s"UIStore: changeNewComment $text")
    uiState.newComment.value = Some(text)
    text
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
                      newComment: Var[Option[String]] = Var(None),
                      webContext: Var[String] = Var("")
                    )

}
