package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import pme123.petstore.shared.{Pet, PetProduct}

import scala.util.matching.Regex

private[client] case class PetProductView(categoryName: String, productIdent: String)
  extends MainView {

  private val petCategory = PetUIStore.changePetCategory(categoryName)

  val link: String = s"${PetProductView.name}/$categoryName/$productIdent"

  // 1. level of abstraction
  // **************************
  @dom
  def create(): Binding[HTMLElement] = {
    // make sure products are initialized
    PetUIStore.uiState.petProductsFor(petCategory).bind
    PetUIStore.changePetProduct(productIdent)
    val maybeProd = PetUIStore.uiState.petProduct.bind
    <div class="">
      {Constants(maybeProd.toSeq.flatMap { prod =>
      Seq(ServerServices.pets(prod)
      ,createAll(prod))
    }.toSeq: _*).map(_.bind)}
    </div>
  }


  // 2. level of abstraction
  // **************************
  @dom
  private def createAll(petProduct: PetProduct): Binding[HTMLElement] = {
    <div class="">
      {productHeader(petProduct).bind}{//
      productDescr(petProduct).bind}{//
      productTable(petProduct).bind}
    </div>
  }

  @dom
  private def productHeader(petProduct: PetProduct) = <h1 class="header">
    <i class={s"product ${petProduct.category.styleName} big icon"}></i> &nbsp; &nbsp;{petProduct.name}

  </h1>

  @dom
  private def productDescr(petProduct: PetProduct) =
    <h4 class="description">
      {Constants(petProduct.tags.map(tagLink).toList: _*).map(_.bind)}
    </h4>

  @dom
  private def productTable(petProduct: PetProduct) = {
    <div class="content">
      <table class="ui padded table">
        <thead>
          <tr>
            <th>
              Name
            </th>
            <th>
              Pet Product
            </th>
            <th>
              Price
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
          {for (pets <- PetUIStore.uiState.pets) yield petRow(pets).bind}
        </tbody>
      </table>
    </div>
  }

  @dom
  private def petRow(pet: Pet) =
    <tr>
      <td class="four wide">
        {pet.itemIdent}
      </td>
      <td class="three wide">
        {pet.product.name}
      </td>
      <td class="two wide right">
        {f"${pet.price}%.2f"}
      </td>
      <td class="five wide">
        {Constants(pet.tags.map(tagLink).toList: _*).map(_.bind)}
      </td>
      <td class="two wide">
        {editButton(pet).bind}{//
        showDetailButton(pet).bind}
      </td>
    </tr>

  @dom
  private def editButton(pet: Pet) = {
    <button class="ui basic icon button"
            onclick={_: Event =>
              info("Edit is not implemented")}
            data:data-tooltip={s"Edit ${pet.itemIdent}"}
            data:data-position="bottom right">
      <i class="edit outline icon"></i>
    </button>
  }

  @dom
  private def showDetailButton(pet: Pet) = {
    <button class="ui basic icon button"
            onclick={_: Event =>
              info("Show Details is not implemented")}
            data:data-tooltip={s"Show ${pet.itemIdent}"}
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

object PetProductView {
  val hashRegex: Regex = """#product/([^/]*)/([^/]*)""".r

  def name: String = "product"

}
