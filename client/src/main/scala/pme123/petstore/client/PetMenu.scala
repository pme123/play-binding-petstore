package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.petstore.client.services.SemanticUI.jq2semantic
import pme123.petstore.client.services.{ClientUtils, SemanticUI}
import pme123.petstore.shared.{PetCategory, PetProduct}

import scala.scalajs.js.timers.setTimeout

private[client] object PetMenu
  extends ClientUtils {

  // 1. level of abstraction
  // **************************
  @dom
  private[client] def create(): Binding[HTMLElement] = {
    <div id="pets" class="ui fluid menu">
      <a class="pets item">
        <h4>Pets Catalog
        <i class="dropdown icon"></i></h4>
      </a>{categoryTable.bind}
    </div>
  }

  // 2. level of abstraction
  // **************************

  @dom
  private lazy val categoryTable = {
    val categories = PetUIStore.uiState.petCategories.bind
    <div class="ui fluid popup bottom left transition hidden">
      <div class={s"ui ${SemanticUI.columnWide(categories.length)} column relaxed divided grid"}>
        {Constants(categories.map(menuItem): _*).map(_.bind)}
      </div>
    </div>
  }


  @dom
  private def menuItem(petCategory: PetCategory): Binding[HTMLElement] = {
    val cat = PetUIStore.uiState.petCategory.bind
    PetUIStore.uiState.allPetProducts.bind
    val products = PetUIStore.uiState.petProductsFor(petCategory)
    <div class="column menuLinks">
      <div class="vertical borderless menu">
        <div class={s"item ${activeStyle(cat == petCategory)}"}>
          <h4
               onclick={_: Event =>
                 PetUIStore.changePetCategory(petCategory)
                 PetUIStore.clearPetProduct()
                 hidePopup}>
            <i class={s"category ${petCategory.styleName} big left icon"}></i>{//
            petCategory.entryName}
          </h4>
        </div>
        {Constants(products.map(productLink): _*).map(_.bind)}
      </div>
    </div>
  }

  @dom
  private def productLink(petProduct: PetProduct) = {
    val prod = PetUIStore.uiState.petProduct.bind

    <div class={s"item ${activeStyle(prod.contains(petProduct))}"}
    onclick={_: Event =>
      info("show Product View")
      PetUIStore.changePetProduct(petProduct)
      PetUIStore.changePetCategory(petProduct.category)
      hidePopup}>
      {petProduct.name}
    </div>
  }
  private def hidePopup = {
    setTimeout(200) {
      jQuery(".pets").popup("hide")
    }
  }

}