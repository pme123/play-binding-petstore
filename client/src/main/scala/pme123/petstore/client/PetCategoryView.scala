package pme123.petstore.client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import org.scalajs.jquery.jQuery
import pme123.petstore.client.PetstoreHeader.info
import pme123.petstore.client.services.ClientUtils
import pme123.petstore.shared.{PetCategory, PetProduct}

import scala.scalajs.js.timers.setTimeout

private[client] object PetCategoryView
  extends ClientUtils {

  // 1. level of abstraction
  // **************************
  @dom
  private[client] def create(): Binding[HTMLElement] = {
    val cat = PetUIStore.uiState.petCategory.bind
    ServerServices.petProducts(cat).bind
    <div class="">
      {categoryHeader(cat).bind}{//
      categoryTable.bind}
    </div>
  }

  // 2. level of abstraction
  // **************************

  @dom
  private def categoryHeader(petCategory: PetCategory) = <h1 class="header">
    <i class={s"category ${petCategory.styleName} big icon"}></i>{petCategory.entryName}

  </h1>

  @dom
  private def categoryTable = {
    <div class="content">
      <table class="ui padded table">
        <thead>
          <tr>
            <th>
              Name
            </th>
            <th>
              Pet Category
            </th>
            <th>
              Tags
            </th>
            <th>
              Actions
            </th>
          </tr>
        </thead>
        <tbody>
          {for (pp <- PetUIStore.uiState.petProducts) yield productRow(pp).bind}
        </tbody>
      </table>
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
        {petProduct.tags.mkString("::")}
      </td>
      <td class="three wide">
        {editButton(petProduct).bind}
      </td>
    </tr>

  @dom
  private def editButton(petProduct: PetProduct) = {
    <div class="ui item">
      <button class="ui basic icon button"
              onclick={_: Event =>
                info("Edit is not implemented")
              }
              data:data-tooltip={s"Edit ${petProduct.name}"}
              data:data-position="bottom right">
        <i class="edit outline icon large"></i>
      </button>
    </div>
  }

}
