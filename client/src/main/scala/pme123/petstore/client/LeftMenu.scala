package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import pme123.petstore.client.services.ClientUtils
import pme123.petstore.shared.PetCategory

private[client] object LeftMenu
  extends ClientUtils {

  // 1. level of abstraction
  // **************************
  @dom
  private[client] def create(): Binding[HTMLElement] = {
    <div class="four wide column">
      <div class="ui basic segment">
        <div class="ui vertical menu">
          {filterHeader.bind}{//
          categorySelect.bind}{//
          petTagSelect.bind}{//
          productTagSelect.bind}
        </div>
      </div>
    </div>
  }

  // 2. level of abstraction
  // **************************
  @dom
  private lazy val filterHeader =
  <div class="item">
    <div class="header">
      <i class={s"search big icon"}></i> &nbsp; &nbsp;
      Filter
    </div>
  </div>

  @dom
  private lazy val categorySelect =
    <div class="item">
      <div class="ui multiple dropdown">
        <input type="hidden" id="categories"
               onchange={_: Event =>
                 UIFilter.changeCategories(categories.value)}/>
        <i class="filter icon"></i>
        <span class="text">Pet Categories</span>
        <div class="menu">
          {Constants(PetCategory.values.map(categoryItem): _*).map(_.bind)}
        </div>
      </div>
    </div>

  @dom
  private def categoryItem(petCategory: PetCategory): Binding[HTMLElement] = {
    <div class="item" data:data-value={petCategory.entryName}>
      <i class={s"category ${petCategory.styleName} big left icon"}></i>{petCategory.entryName}
    </div>
  }

  @dom
  private lazy val petTagSelect = {
    ServerServices.petTags().bind
    <div class="item">
      <div class="ui multiple dropdown">
        <input type="hidden"
               id="petTags"
               onchange={_: Event =>
                 UIFilter.changePetTags(petTags.value)}/>
        <i class="filter icon"></i>
        <span class="text">Pet Tags</span>
        <div class="menu">
          {Constants(PetUIStore.uiState.petTags.value.map(petTagItem): _*).map(_.bind)}
        </div>
      </div>
    </div>
  }


  @dom
  private def petTagItem(tag: String): Binding[HTMLElement] = {
    <div class="item" data:data-value={tag}>
      {tag}
    </div>
  }

  @dom
  private lazy val productTagSelect = {
    ServerServices.productTags().bind
    <div class="item">
      <div class="ui multiple dropdown">
        <input type="hidden"
               id="productTags"
               onchange={_: Event =>
                 UIFilter.changeProductTags(productTags.value)}/>
        <i class="filter icon"></i>
        <span class="text">Pet Tags</span>
        <div class="menu">
          {Constants(PetUIStore.uiState.productTags.value.map(productTagItem): _*).map(_.bind)}
        </div>
      </div>
    </div>
  }


  @dom
  private def productTagItem(tag: String): Binding[HTMLElement] = {
    <div class="item" data:data-value={tag}>
      {tag}
    </div>
  }
}
