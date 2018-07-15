package pme123.petstore.client

import com.thoughtworks.binding.Binding
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.client.services.ClientUtils

trait MainView
  extends ClientUtils {

  def link: String

  def create(): Binding[HTMLElement]

}

