package pme123.petstore.client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.petstore.client.services.ClientUtils
import pme123.petstore.client.services.SemanticUI.jq2semantic

import scala.language.implicitConversions
import scala.scalajs.js.timers.setTimeout

private[client] object PetstoreHeader
  extends ClientUtils {

  // 1. level of abstraction
  // **************************
  @dom
  private[client] def create(): Binding[HTMLElement] = {
    <div class="ui main borderless menu">
      {faviconElem.bind}{//
      title.bind //
      }{spacer.bind}{//
      PetMenu.create().bind}{//
      shoppingCardButton.bind}{//
      logInButton.bind}
    </div>

  }

  // 2. level of abstraction
  // **************************

  @dom
  private def title = {
    <div class="item">
      <div class="content">
        <h4 class="header">PetStore</h4>
        <div class="meta">Play - Binding Demo</div>
      </div>
    </div>
  }

  @dom
  private def spacer = {
    <div class="item">
      <div class="content">
        <h4 class="header">
          &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
        </h4>
        <div class="meta"></div>
      </div>
    </div>
  }

  @dom
  private def shoppingCardButton = {
    <div class="ui item">
      <button class="ui basic icon button"
              onclick={_: Event =>
                info("Shopping Card is not implemented")}
              data:data-tooltip="Shopping Cart"
              data:data-position="bottom right">
        <i class="shopping cart icon large"></i>
      </button>
    </div>
  }

  @dom
  private def logInButton = {
    <div class="ui item">
      <button class="ui basic icon button"
              onclick={_: Event =>
                info("LOG IN is not implemented")
                setTimeout(200) {
                  jQuery(".ui.modal").modal("show")
                }}
              data:data-tooltip="Log In"
              data:data-position="bottom right">
        <i class="sign in alternate icon large"></i>
      </button>
    </div>
  }


}
