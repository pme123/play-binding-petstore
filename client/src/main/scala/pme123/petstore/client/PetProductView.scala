package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.shared.PetProduct

import scala.util.matching.Regex

private[client] case class PetProductView(categoryName: String, productIdent: String)
  extends PetTable
    with CategoryView {

  val link: String = s"${PetProduct.name}/$categoryName/$productIdent"

  // 1. level of abstraction
  // **************************
  @dom
  protected def createView(): Binding[HTMLElement] = {
    // make sure products are initialized
    val products = PetUIStore.uiState.petProductsFor(categoryName).bind
    if (products.nonEmpty) {
      PetUIStore.changePetProduct(productIdent)
      val maybeProd = PetUIStore.uiState.petProduct.bind
      <div class="">
        {Constants(maybeProd.toSeq.flatMap { prod =>
        Seq(ServerServices.pets(prod)
          , createAll(prod))
      }: _*).map(_.bind)}
      </div>
    } else <div>
      {loadingElem.bind}
    </div>

  }


  // 2. level of abstraction
  // **************************
  @dom
  private def createAll(petProduct: PetProduct): Binding[HTMLElement] = {
    <div class="">
      {productHeader(petProduct).bind}{//
      productDescr(petProduct).bind}{//
      if(PetUIStore.uiState.pets.bind.isEmpty){
        <div class="content">
          <h3><i class="thumbs down outline big icon"/>Sorry at the moment there is nothing in stock.</h3>
        </div>
      } else petTable.bind}
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
}
