package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.dom
import org.scalajs.dom.raw.Event
import pme123.petstore.client.services.UIStore
import pme123.petstore.shared.Pet

private[client] trait PetTable
  extends MainView {


  // 2. level of abstraction
  // **************************

  @dom
  private[client] lazy val petTable = {
    <div class="content">
      <table class="ui padded table">
        <thead>
          <tr>
            <th>
              Description
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
  private def petRow(pet: Pet) = {
    <tr>
      <td class="five wide">
        {petLink(pet).bind}
      </td>
      <td class="three wide">
        {productLink(pet.product).bind}
      </td>
      <td class="two wide right">
        {f"${pet.price}%.2f"}
      </td>
      <td class="four wide">
        {Constants(pet.tags.map(tagLink).toList: _*).map(_.bind)}
      </td>
      <td class="two wide">
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


}


