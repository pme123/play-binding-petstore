package pme123.petstore.client

import com.thoughtworks.binding.Route
import org.scalajs.dom.window
import pme123.petstore.client.PetstoreClient.info
import pme123.petstore.client.services.ClientWebsocket

object UIRoute {


  def createView(hashText: String): MainView = {
    ClientWebsocket.send(hashText)
    hashText match {
      case PetFilterView.hashRegex() =>
        info(s"PetFilterView")
        PetFilterView
      case PathMsgView.hashPath =>
        info(s"PathMsgView")
        PathMsgView
      case PetView.hashRegex(idCat, idProd, id) =>
        info(s"PetView: $idCat $idProd $id")
        PetView(idCat, idProd, id)
      case PetProductView.hashRegex(idCat, id) =>
        info(s"PetProductView: $idCat $id")
        PetProductView(idCat, id)
      case PetCategoryView.hashRegex(id) =>
        info(s"PetCategoryView: $id")
        PetCategoryView(id)
      case _ =>
        info(s"PetFilterView!!: $hashText")
        PetFilterView
    }
  }

  def changeRoute(view: MainView) {
    if (route.state.value != view)
      route.state.value = view
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
}
