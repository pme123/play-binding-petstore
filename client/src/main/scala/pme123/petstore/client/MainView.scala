package pme123.petstore.client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.client.services.ClientUtils
import pme123.petstore.shared.{PetCategory, PetProduct}

trait MainView
  extends ClientUtils {

  def link: String

  def create(): Binding[HTMLElement]

  @dom
  def categoryLink(petCategory: PetCategory): Binding[HTMLElement] = {
    val catLink = s"#${PetCategoryView.name}/${petCategory.ident}"
    <a href={catLink}>
      {petCategory.name}
    </a>
  }

  @dom
  def productLink(petProduct: PetProduct): Binding[HTMLElement] = {
    val prodIdent = s"${petProduct.category.ident}/${petProduct.productIdent}"
    val prodLink = s"#${PetProductView.name}/$prodIdent"
    <a href={prodLink}>
      {petProduct.name}
    </a>
  }

}

