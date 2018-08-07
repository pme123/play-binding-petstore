package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import pme123.petstore.client.services.ClientUtils
import pme123.petstore.shared.{PetCategory, PetProduct}

import scala.util.matching.Regex

trait CategoryView
  extends ClientUtils {
  def categoryName: String

  @dom
  def create(): Binding[HTMLElement] = {
    val categories = PetUIStore.uiState.petCategories.bind
    // make sure categories are initialized
    if (categories.nonEmpty) {
      PetUIStore.changePetCategory(categoryName)
      <div>
        {createView().bind}
      </div>
    } else <div>
      {loadingElem.bind}
    </div>

  }

  protected def createView(): Binding[HTMLElement]
}

private[client] case class PetCategoryView(categoryName: String)
  extends MainView
    with CategoryView {

  val link: String = s"${PetCategory.name}/$categoryName"

  // 1. level of abstraction
  // **************************
  @dom
  protected def createView(): Binding[HTMLElement] = {

    val maybeCat = PetUIStore.uiState.petCategory.bind
    <div class="">
      {Constants(maybeCat.toSeq.flatMap { cat =>
      Seq(categoryHeader(cat),
        categoryDescr(cat),
        categoryTable(cat)
      )
    }: _*).map(_.bind)}
    </div>
  }

  // 2. level of abstraction
  // **************************

  @dom
  private def categoryHeader(petCategory: PetCategory) = <h1 class="header">
    <i class={s"category ${
      petCategory.styleName
    } big icon"}></i> &nbsp; &nbsp;{petCategory.ident}
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
          {for (pp <- PetUIStore.uiState.petProductsFor(petCategory.ident)) yield productRow(pp).bind}
        </tbody>
      </table>
    </div>
  }

  @dom
  private def productRow(petProduct: PetProduct) =
    <tr>
      <td class="five wide">
        <a href={s"#product/${
          petProduct.category.ident
        }/${
          petProduct.productIdent
        }"}>
          {petProduct.name}
        </a>
      </td>
      <td class="three wide">
        {petProduct.category.ident}
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
              info("Edit is not implemented")} data:data-tooltip={s"Edit ${
      petProduct.name
    }"} data:data-position="bottom right">
      <i class="edit outline icon"></i>
    </button>
  }

  @dom
  private def showDetailButton(petProduct: PetProduct) = {
    <button class="ui basic icon button"
            onclick={_: Event =>
              info("Show Details is not implemented")} data:data-tooltip={s"Show ${
      petProduct.name
    }"} data:data-position="bottom right">
      <i class="external alternate icon"></i>
    </button>
  }
}

object PetCategoryView {
  val hashRegex: Regex = """#category/([^/]*)""".r

}
