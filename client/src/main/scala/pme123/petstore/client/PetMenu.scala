package pme123.petstore.client

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
          <i class="dropdown icon"></i>
        </h4>
      </a>{categoryTable.bind}
    </div>
  }

  // 2. level of abstraction
  // **************************

  @dom
  private lazy val categoryTable = {
    <div class="ui fluid popup bottom left transition hidden">
      <div class={s"ui ${SemanticUI.columnWide(PetUIStore.uiState.petCategories.bind.length)} column relaxed divided grid"}>
        {for (category <- PetUIStore.uiState.petCategories) yield menuItem(category).bind}
      </div>
    </div>
  }


  @dom
  private def menuItem(petCategory: PetCategory): Binding[HTMLElement] = {
    val cat = PetUIStore.uiState.petCategory.bind
    <div class="column menuLinks">
      <div class="vertical borderless menu">
        <div class={s"item ${activeStyle(cat.contains(petCategory))}"}>
          <h4>
            <a
            href={petCategory.link}
            onclick={_: Event =>
              PetUIStore.changePetCategory(petCategory)
              PetUIStore.clearPetProduct()
              hidePopup}>
              <i class={s"category ${petCategory.styleName} big left icon"}></i>{//
              petCategory.ident}
            </a>
          </h4>
        </div>{for(product <- PetUIStore.uiState.petProductsFor(petCategory)) yield productLink(product).bind}
      </div>
    </div>
  }

  @dom
  private def productLink(petProduct: PetProduct) = {
    val prod = PetUIStore.uiState.petProduct.bind

    <a class={s"item ${activeStyle(prod.contains(petProduct))}"}
       onclick={_: Event =>
         info("show Product View")
         PetUIStore.changePetProduct(petProduct)
         PetUIStore.changePetCategory(petProduct.category)
         hidePopup}
       href={petProduct.link}>
      {petProduct.name}
    </a>
  }

  private def hidePopup = {
    setTimeout(200) {
      jQuery(".pets").popup("hide")
    }
  }

}
