package pme123.petstore.client.services

import com.thoughtworks.binding.Binding.Var
import pme123.petstore.shared.services.Logging

import scala.language.implicitConversions

object UIStore extends Logging {

  val uiState = UIState()

  def changeWebContext(webContext: String): String = {
    info(s"UIStore: changeWebContext $webContext")
    uiState.webContext.value = webContext
    webContext
  }

  case class UIState(
                      webContext: Var[String] = Var("")
                    )


}