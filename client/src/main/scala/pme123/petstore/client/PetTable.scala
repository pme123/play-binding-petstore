package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.dom
import org.scalajs.dom.raw.Event
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
      <td class="five wide">
        {pet.descr}
      </td>
      <td class="three wide">
        <a href={s"#product/${pet.product.category.entryName}/${pet.product.productIdent}"}>{pet.product.name}</a>
      </td>
      <td class="two wide right">
        {f"${pet.price}%.2f"}
      </td>
      <td class="four wide">
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


}


