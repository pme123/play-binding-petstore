package pme123.petstore.client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.shared.PathMsg

import scala.scalajs.js.Date

private[client] object PathMsgView
  extends MainView {

  val hashPath: String = "#pathMsg"

  val link: String = s"pathMsg"

  // 1. level of abstraction
  // **************************

  @dom
  def create(): Binding[HTMLElement] = {
    <div>
      <table class="ui very basic table">
        <thead>
          <tr>
            <th class="three wide">User</th>
            <th class="ten wide">Path</th>
            <th class="three wide">Time</th>
          </tr>
        </thead>
        <tbody>
          {for (pathMsg <- PetUIStore.uiState.pathMsgs) yield createView(pathMsg).bind}
        </tbody>
      </table>
    </div>

  }

  @dom
  protected def createView(pathMsg: PathMsg): Binding[HTMLElement] = {
    <tr>
      <td>
        {pathMsg.username}
      </td> <td>
      {pathMsg.msg}
    </td> <td>
      {new Date(pathMsg.time).toLocaleString()}
    </td>
    </tr>
  }
}

