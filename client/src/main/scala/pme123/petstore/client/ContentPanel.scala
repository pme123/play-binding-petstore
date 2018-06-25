package pme123.petstore.client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.client.services.ClientUtils

private[client] object ContentPanel
  extends ClientUtils {

  // 1. level of abstraction
  // **************************
  @dom
  private[client] def create(): Binding[HTMLElement] = {
    <div class="twelve wide column">
      <div class="ui basic segment">
        {PetCategoryView.create().bind}
      </div>
    </div>
  }

  // 2. level of abstraction
  // **************************

}
