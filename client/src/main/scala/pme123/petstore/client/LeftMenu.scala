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
        <div class="ui fluid vertical menu">
          {filterHeader.bind}{//
          petDescrInput.bind}{//
          productInput.bind}{//
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
  private lazy val petDescrInput =
    <div class="item">
      <div class="ui input">
        <input id="petDescrFilter"
               type="text"
               placeholder="Pet Description..."
               onkeyup={_: Event =>
                 UIFilter.changePetDescr(petDescrFilter.value)}/>
      </div>
    </div>

  @dom
  private lazy val productInput =
    <div class="item">
      <div class="ui input">
        <input id="productFilter"
               type="text"
               placeholder="Product..."
               onkeyup={_: Event =>
                 UIFilter.changeProduct(productFilter.value)}/>
      </div>
    </div>

  @dom
  private lazy val categorySelect =
    <div class="item">
      <div class="ui multiple dropdown">
        <input type="hidden" id="categoriesFilter"
               onchange={_: Event =>
                 UIFilter.changeCategories(categoriesFilter.value)}/>
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
               id="petTagsFilter"
               onchange={_: Event =>
                 UIFilter.changePetTags(petTagsFilter.value)}/>
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
               id="productTagsFilter"
               onchange={_: Event =>
                 UIFilter.changeProductTags(productTagsFilter.value)}/>
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
