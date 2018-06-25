package pme123.petstore.client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.client.services.ClientUtils
import pme123.petstore.shared.{PetCategory, PetProduct}

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
              Product Ident
            </th>
            <th>
              Name
            </th>
            <th>
              Pet Category
            </th>
            <th>
              Tags
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
      <td class="three wide">
        {petProduct.productIdent}
      </td>
      <td class="five wide">
        {petProduct.name}
      </td>
      <td class="three wide">
        {petProduct.category.entryName}
      </td>
      <td class="five wide">
        {petProduct.tags.mkString("::")}
      </td>
    </tr>

}
