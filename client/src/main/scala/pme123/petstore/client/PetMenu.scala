package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.petstore.client.services.{ClientUtils, SemanticUI}
import pme123.petstore.shared.{PetCategory, PetProduct}
import pme123.petstore.client.services.SemanticUI.jq2semantic

import scala.scalajs.js
import scala.scalajs.js.timers.setTimeout

private[client] object PetMenu
  extends ClientUtils {

  // 1. level of abstraction
  // **************************
  @dom
  private[client] def create(): Binding[HTMLElement] = {
    ServerServices.petCategories().bind
    <div class="ui fluid menu">
      <a class="pets item">
        Pets Catalog
        <i class="dropdown icon"></i>
        &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
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
        {Constants(categories.map(menuItem):_*).map(_.bind)}
      </div>
    </div>
  }

  @dom
  private def menuItem(petCategory: PetCategory): Binding[HTMLElement] = {
    val cat = PetUIStore.uiState.petCategory.bind
    <div class={s"column ${activeStyle(cat == petCategory)}"}
         onclick={_: Event =>
           PetUIStore.changePetCategory(petCategory)
           setTimeout(200) {
             jQuery(".pets").popup("hide")
           }}>
      <h4>
        <i class={s"category ${petCategory.styleName} big left icon"}></i>{petCategory.entryName}
      </h4>
      <p>
        {petCategory.subTitle}
      </p>
    </div>
  }


  @dom
  private def productRow(petProduct: PetProduct) =
    <tr>
      <td class="five wide">
        {petProduct.name}
      </td>
      <td class="three wide">
        {petProduct.category.entryName}
      </td>
      <td class="five wide">
        {Constants(petProduct.tags.map(tagLink).toList: _*).map(_.bind)}
      </td>
      <td class="three wide">
        {editButton(petProduct).bind}{//
        showDetailButton(petProduct).bind}
      </td>
    </tr>

  @dom
  private def editButton(petProduct: PetProduct) = {
    <button class="ui basic icon button"
            onclick={_: Event =>
              info("Edit is not implemented")}
            data:data-tooltip={s"Edit ${petProduct.name}"}
            data:data-position="bottom right">
      <i class="edit outline icon"></i>
    </button>
  }

  @dom
  private def showDetailButton(petProduct: PetProduct) = {
    <button class="ui basic icon button"
            onclick={_: Event =>
              info("Show Details is not implemented")}
            data:data-tooltip={s"Show ${petProduct.name}"}
            data:data-position="bottom right">
      <i class="external alternate icon"></i>
    </button>
  }

  @dom
  private def tagLink(tag: String) = {
    <a class="ui tag label">
      {tag}
    </a>

  }


}
