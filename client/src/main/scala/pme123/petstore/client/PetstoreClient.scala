package pme123.petstore.client

import com.thoughtworks.binding.Binding.{Constants, Var}
import com.thoughtworks.binding.{Binding, Route, dom}
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.jquery.jQuery
import pme123.petstore.client.services.SPAClient
import pme123.petstore.client.services.SemanticUI.jq2semantic
import pme123.petstore.shared.PetCategory
import org.scalajs.dom.window

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.scalajs.js.timers.setTimeout

object PetstoreClient
  extends SPAClient {

  private val homeMainView = PetCategoryView

  def createView(hashText: String): MainView = hashText match {
    case PetProductView.hashRegex(idCat, id) =>
      info(s"PetProductView: $idCat $id")
      PetProductView(idCat, id)
    case PetCategoryView.hashRegex(id) =>
      info(s"PetCategoryView: $id")
      PetCategoryView(id)
    case _ =>
      info(s"PetCategoryView!!: $hashText")
      PetCategoryView(PetCategory.Dogs.entryName)
  }

  info("org.scalajs.dom.window.location: " + window.location)

  val route: Route.Hash[MainView] = Route.Hash[MainView](createView(window.location.hash))(
    new Route.Format[MainView] {
      override def unapply(hashText: String): Option[MainView] = {
        Some(createView(hashText))
      }

      override def apply(state: MainView): String = {
        info(s"state.name > ${state.link}")
        state.link
      }
    }
  )

  route.watch()


  val mainView: Var[MainView] = route.state

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
      {initCategories.bind}{//
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


