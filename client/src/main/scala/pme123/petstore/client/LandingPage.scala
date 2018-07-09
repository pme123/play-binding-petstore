package pme123.petstore.client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.document
import org.scalajs.dom.raw._
import org.scalajs.jquery.jQuery
import pme123.petstore.client.services.SemanticUI.jq2semantic
import pme123.petstore.shared.services.Logging

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.timers.setTimeout

case class LandingPage() extends Logging {

  // 1. level of abstraction
  // **************************

  def create(): Unit = {
    dom.render(document.body, render)
    jQuery(".ui.dropdown").dropdown(js.Dynamic.literal(on = "hover"))
    jQuery(".ui.button").popup(js.Dynamic.literal(inline = true))
    setTimeout(200) {
      jQuery(".pets").popup(js.Dynamic.literal(on = "click"))
    }

  jQuery(".ui.item .ui.input").popup(js.Dynamic.literal(on = "hover"))
}

@dom
def render: Binding[HTMLElement] = {
  <div class="">{PetstoreHeader.create ().bind}{//
  PetMenu.create ().bind}<div class="ui four column doubling stackable grid">{//
  LeftMenu.create ().bind}{//
  ContentPanel.create ().bind}</div>
  </div>
}

}