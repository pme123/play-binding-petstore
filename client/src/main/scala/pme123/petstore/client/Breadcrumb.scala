package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.shared.{Pet, PetCategory, PetProduct}

object Breadcrumb {

  @dom
  def create(): Binding[HTMLElement] = <div class="ui basic segment">

    {breadcrumbElem().bind}
  </div>


  @dom
  private def breadcrumbElem(): Binding[HTMLElement] = {
    val route = UIRoute.route.state.bind
    route match {
      case PetFilterView => <div class="ui breadcrumb">
        <i class="right arrow icon divider"></i>
        <div class="active section">Filter</div>
      </div>
      case _: PetCategoryView =>
        val category = PetUIStore.uiState.petCategory.bind
        <div class="ui breadcrumb">
          <i class="right arrow icon divider"></i>{//
          Constants(category.toList.map(categoryActive): _*).map(_.bind)}
        </div>
      case ppv: PetProductView =>
        val product = PetUIStore.uiState.petProduct.bind
        <div class="ui breadcrumb">
          {Constants(product.toList.map(pp => categoryLink(pp.category)): _*).map(_.bind) //
          }<i class="right arrow icon divider"></i>{//
          Constants(product.toList.map(productActive): _*).map(_.bind)}
        </div>
      case pv: PetView =>
        val pet = PetUIStore.uiState.pet.bind.maybePet
        <div class="ui breadcrumb">
          {Constants(pet.toList.map(p => categoryLink(p.product.category)): _*).map(_.bind) //
          }<i class="right chevron icon divider"></i>{//
          Constants(pet.toList.map(p => productLink(p.product)): _*).map(_.bind) //
          }<i class="right arrow icon divider"></i>{//
          Constants(pet.toList.map(petActive): _*).map(_.bind)}
        </div>
    }


  }

  @dom
  private def categoryActive(category: PetCategory): Binding[HTMLElement] =
    <div class="active section">
      {category.name}
    </div>

  @dom
  private def categoryLink(category: PetCategory): Binding[HTMLElement] =
    <a class="section" href={category.link}>
      {category.name}
    </a>

  @dom
  private def productActive(product: PetProduct): Binding[HTMLElement] =
    <div class="active section">
      {product.name}
    </div>

  @dom
  private def productLink(product: PetProduct): Binding[HTMLElement] =
    <a class="section" href={product.link}>
      {product.name}
    </a>

  @dom
  private def petActive(pet: Pet): Binding[HTMLElement] =
    <div class="active section">
      {pet.descr}
    </div>
}

