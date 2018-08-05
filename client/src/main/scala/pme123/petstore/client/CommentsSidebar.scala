package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.client.PetstoreHeader.staticAsset
import pme123.petstore.client.services.{LoggedInUser, UIStore}
import pme123.petstore.shared.services.{Comment, InstantHelper}

object CommentsSidebar
  extends InstantHelper {

  @dom
  def create(): Binding[HTMLElement] = {
    val user = UIStore.uiState.loggedInUser.bind
    <div class="ui labeled icon right inline vertical demo sidebar menu">
      <div class="item header">
        <i class="home icon"></i>
        Talk with us
      </div> <div class="item">
      {//
      if (user.isDefined) {
        ServerServices.commentsFor(user.username).bind
        val comments = UIStore.uiState.comments.bind
        commentsElems(comments).bind
      }
      else
        <div
        class="ui comments"></div>}
      <form class="ui reply form">
        <div class="field">
          <textarea></textarea>
        </div>
        <div class="ui blue labeled submit icon button">
          <i class="icon edit"></i> Send Message
        </div>
      </form>
    </div>
    </div>
  }

  @dom
  private def commentsElems(comments: Seq[Comment]): Binding[HTMLElement] = {
    <div class="ui comments">
      {Constants(comments.map(commentElem): _*).map(_.bind)}
    </div>
  }

  @dom
  private def commentElem(comment: Comment): Binding[HTMLElement] = {
    val user = LoggedInUser(Some(comment.user))
    <div class="ui comment">
      <a class="avatar">
        <img class="mini circular image" src={staticAsset(user.avatar)}/>
      </a>
      <div class="content">
        <a class="author">
          {user.fullName}
        </a>
        <div class="metadata">
          <span class="date">
            {comment.created.toString}
          </span>
        </div>
        <div class="text">
          {comment.text}
        </div>
        <div class="actions">
          <a class="reply">
            Reply
          </a>
        </div>
      </div>
    </div>
  }
}
