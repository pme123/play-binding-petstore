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

  def changeConversations(conversations: Conversations): Conversations = {
    info(s"UIStore: changeConversations for ${conversations.conversations.length}")
    uiState.conversations.value.clear()
    uiState.conversations.value ++= conversations.conversations
    conversations
  }

  case class UIState(
                      loggedInUser: Var[LoggedInUser] = Var(LoggedInUser()),
                      conversations: Vars[Conversation] = Vars(),
                      newComment: Var[Option[String]] = Var(None),
                      webContext: Var[String] = Var("")
                    )

}
