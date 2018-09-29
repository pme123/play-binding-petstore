package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.petstore.client.PetstoreHeader.staticAsset
import pme123.petstore.client.services.SemanticUI.{Field, Form, Rule, jq2semantic}
import pme123.petstore.client.services.UIStore
import pme123.petstore.shared.services.{Comment, Conversation, InstantHelper, LoggedInUser}

import scala.scalajs.js
import scala.scalajs.js.timers.setTimeout

object CommentsSidebar
  extends InstantHelper {

  val form: js.Object = new Form {
    //noinspection TypeAnnotation
    val fields = js.Dynamic.literal(
      commentField = new Field {
        val identifier: String = "commentField"
        val rules: js.Array[Rule] = js.Array(new Rule {
          val `type`: String = "minLength[5]"
        })
      }
    )
  }

  @dom
  def create(): Binding[HTMLElement] = {

    <div class="ui labeled icon right inline vertical demo sidebar menu">
      <div class="item header">
        <i class="home icon"></i>
        Talk with us
      </div> <div class="item">
      {//
      Constants(UIStore.uiState.loggedInUser.bind.maybeUser.toSeq: _*)
        .map(user => comments(user.username))
        .map(_.bind) //
      }<iframe style="display:none" onload={_: Event => initForm()}></iframe>
      <form id="commentForm" class="ui reply form" onsubmit={event: Event =>
        if (jQuery("#commentForm").form("is valid").asInstanceOf[Boolean])
          UIStore.changeNewComment(jQuery("#commentField").value.toString)
        event.preventDefault()}>
        <div class="ui error message"></div>{Constants(UIStore.uiState.newComment.bind.toSeq: _*)
        .map(ServerServices.addComment(_).bind)}<div class="field">
        <textarea id="commentField"></textarea>
      </div>
        <button type="submit" id="commentButton" class="ui blue labeled fluid submit icon button">
          <i class="icon edit"></i>
          Send Message
        </button>
      </form>
    </div>
    </div>
  }

  @dom
  private def comments(username: String): Binding[HTMLElement] = {
    <div>
      {ServerServices.commentsFor(username).bind //
      }<div class="ui comments">
      {//
      for (comment <- UIStore.uiState.conversations) yield conversationElem(comment).bind}
    </div>
    </div>
  }

  @dom
  private def conversationElem(conv: Conversation): Binding[HTMLElement] =
    <div>
      {//
      Constants(conv.comments: _*).map(commentElem(_).bind)}
    </div>


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

  private def initForm(): Unit = {
    setTimeout(200) {
      jQuery("#commentForm").form(CommentsSidebar.form)
    }
  }
}
