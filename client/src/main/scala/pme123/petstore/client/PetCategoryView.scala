package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import pme123.petstore.shared.{PetCategory, PetProduct}

import scala.util.matching.Regex

private[client] case class PetCategoryView(categoryName: String)
  extends MainView {

  PetUIStore.changePetCategory(categoryName)

  val link: String = s"${PetCategoryView.name}/$categoryName"

  // 1. level of abstraction
  // **************************
  @dom
  def create(): Binding[HTMLElement] = {
    val cat = PetUIStore.uiState.petCategory.bind
    <div class="">
      {categoryHeader(cat).bind}{//
      categoryDescr(cat).bind}{//
      categoryTable(cat).bind}
    </div>
  }

  // 2. level of abstraction
  // **************************

  @dom
  private def categoryHeader(petCategory: PetCategory) = <h1 class="header">
    <i class={s"category ${petCategory.styleName} big icon"}></i> &nbsp; &nbsp;{petCategory.entryName}

  </h1>

  @dom
  private def categoryDescr(petCategory: PetCategory) =
    <h4 class="description">
      {petCategory.subTitle}
    </h4>

  @dom
  private def categoryTable(petCategory: PetCategory) = {
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
          {for (pp <- PetUIStore.uiState.petProductsFor(petCategory)) yield productRow(pp).bind}
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

object PetCategoryView {
  val hashRegex: Regex = """#category/([^/]*)""".r

  def name: String = "category"

}
