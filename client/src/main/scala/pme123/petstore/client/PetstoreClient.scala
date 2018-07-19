package pme123.petstore.client

import com.thoughtworks.binding.Binding.{Constants, Var}
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.jquery.jQuery
import pme123.petstore.client.services.SPAClient
import pme123.petstore.client.services.SemanticUI.jq2semantic
import pme123.petstore.shared.PetCategory

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js.timers.setTimeout

object PetstoreClient
  extends SPAClient {


  val mainView: Var[MainView] = UIRoute.route.state

  // @JSExportTopLevel exposes this function with the defined name in Javascript.
  // this is called by the index.scala.html of the server.
  // the only connection that is not type-safe!
  @JSExportTopLevel("client.PetstoreClient.main")
  def main(context: String) {
    initClient(context)


    dom.render(document.body, render)
    setTimeout(200) {
      jQuery(".ui.dropdown").dropdown(js.Dynamic.literal(on = "hover"))
      jQuery(".ui.button").popup(js.Dynamic.literal(inline = true))
      jQuery(".pets").popup(js.Dynamic.literal(on = "click"))
      jQuery(".ui.item .ui.input").popup(js.Dynamic.literal(on = "hover"))

    }
  }

  @dom
  def render: Binding[HTMLElement] = {

    <div class="">
      {//
      ServerServices.runFilter.bind}{//
      initCategories.bind}{//
      PetstoreHeader.create().bind}<div class="ui four column doubling stackable grid">
      {//
      LeftMenu.create().bind //
      }<div class="twelve wide column">
        <div class="ui basic segment">
          {mainView.bind.create().bind}
        </div>
      </div>
    </div>
    </div>
  }


  @dom
  private lazy val initCategories: Binding[HTMLElement] = {
    <div>
      {Constants(PetCategory.values.map(initProducts): _*).map(_.bind)}
    </div>
  }

  @dom
  private def initProducts(petCategory: PetCategory): Binding[HTMLElement] = {
    <div>
      {ServerServices.petProducts(petCategory).bind}
    </div>
  }

}


