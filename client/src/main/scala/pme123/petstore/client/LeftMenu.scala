package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import pme123.petstore.client.services.ClientUtils
import pme123.petstore.shared.PetCategory

private[client] object LeftMenu
  extends ClientUtils {

  // 1. level of abstraction
  // **************************
  @dom
  private[client] def create(): Binding[HTMLElement] = {
    <div class="four wide column">
      <div class="ui basic segment">
        <div class="ui vertical pointing menu">
          {Constants(PetCategory.values.map(menuItem): _*).map(_.bind)}
        </div>
      </div>
    </div>
  }

  // 2. level of abstraction
  // **************************

  @dom
  private def menuItem(petCategory: PetCategory): Binding[HTMLElement] = {
    val cat = PetUIStore.uiState.petCategory.bind
    <a
    href={s"#${PetCategoryView.name}/${petCategory.entryName}"}>
      <div class={s"item ${activeStyle(cat == petCategory)}"}>


        <i class={s"category ${petCategory.styleName} big left icon"}></i>
        <div class="header">
          {petCategory.entryName}
        </div>
        <div class="description">
          {petCategory.subTitle}
        </div>
      </div>
    </a>

  }
}
