package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.{Event, HTMLElement}
import pme123.petstore.client.services.UIStore
import pme123.petstore.shared.Pet

private[client] trait PetTable
  extends MainView {


  // 2. level of abstraction
  // **************************

  @dom
  private[client] lazy val petTable = {
    <div class="content">
      <table class="ui very basic table">
        <tbody>
          {for (pets <- PetUIStore.uiState.pets) yield petRow(pets).bind}
        </tbody>
      </table>
    </div>
  }

  @dom
  private def petRow(pet: Pet) = {
    <tr>
      <td class="six wide">
        {petLink(pet).bind}
      </td>
      <td class="three wide">
        {productLink(pet.product).bind}
      </td>
      <td class="two wide right">
        {pet.priceAsStr}
      </td>
      <td class="four wide">
        {Constants(pet.tags.map(tagLink).toList: _*).map(_.bind)}
      </td>
      <td class="one wide left">
        {editButton(pet).bind}{//
        addToCardButton(pet).bind}
      </td>
    </tr>
  }

  @dom
  private def editButton(pet: Pet) = {
    val user = UIStore.uiState.loggedInUser.bind
    if (user.isManager)
      <button class="ui basic icon button"
              onclick={_: Event =>
                info("Edit is not implemented")}
              data:data-tooltip={s"Edit ${pet.descr}"}
              data:data-position="bottom right">
        <i class="edit outline icon"></i>
      </button>
    else <span></span>
  }

  @dom
  private def addToCardButton(pet: Pet) = {
    val user = UIStore.uiState.loggedInUser.bind
    if (user.isCustomer)
      <button class="ui basic icon button"
              onclick={_: Event =>
                info("Add to Card is not implemented")}
              data:data-tooltip={s"Add ${pet.descr} to the Shopping Card"}
              data:data-position="bottom right">
        <i class="shopping cart icon"></i>
      </button>
    else <span></span>
  }

  @dom
  def petLink(pet: Pet): Binding[HTMLElement] = {
    val prodIdent = s"${pet.product.category.ident}/${pet.product.productIdent}"
    val petLink = s"#${PetView.name}/$prodIdent/${pet.itemIdent}"
    <a href={petLink}>
      <div class="ui mini circular image">
        <img src={staticAsset(pet.firstPhotoUrl)}/>
      </div>
      &nbsp;&nbsp;{pet.descr}
    </a>
  }

}


