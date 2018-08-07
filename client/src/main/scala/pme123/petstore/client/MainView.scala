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
    <a href={petCategory.link}>
      {petCategory.name}
    </a>
  }

  @dom
  def productLink(petProduct: PetProduct): Binding[HTMLElement] = {
    <a href={petProduct.link}>
      {petProduct.name}
    </a>
  }

}

