package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import pme123.petstore.shared.{Pet, PetProduct}

import scala.util.matching.Regex

private[client] case class PetProductView(categoryName: String, productIdent: String)
  extends PetTable {

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
      petTable.bind}
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

}

object PetProductView {
  val hashRegex: Regex = """#product/([^/]*)/([^/]*)""".r

  def name: String = "product"

}
