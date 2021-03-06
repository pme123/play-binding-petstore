package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.shared.Pet

import scala.util.matching.Regex

private[client] case class PetView(categoryName: String, productIdent: String, petIdent: String)
  extends MainView {

  val link: String = s"${Pet.name}/$categoryName/$productIdent/$petIdent"

  // 1. level of abstraction
  // **************************
  @dom
  def create(): Binding[HTMLElement] = {
    // make sure products are initialized
    <div>
      {ServerServices.pet(petIdent).bind}{//
      val maybePet = PetUIStore.uiState.pet.bind
      if (maybePet.nonEmpty) {
        <div class="">
          {createAll(maybePet.pet).bind}
        </div>
      } else <div>
        {loadingElem.bind}
      </div>}
    </div>
  }


  // 2. level of abstraction
  // **************************
  @dom
  private def createAll(pet: Pet): Binding[HTMLElement] = {
    <div class="ui grid">
      <div class="ten wide column">
        {imagePanel(pet).bind}
      </div>
      <div class="six wide column">
        {petHeader(pet).bind}{//
        petDescr(pet).bind}{//
        petPrice(pet).bind}{//
        addToCardButton(pet).bind}{//
        petInfo(pet).bind}
      </div>
    </div>
  }

  @dom
  private def petHeader(pet: Pet) = <h2 class="header">
    {pet.descr}
  </h2>

  @dom
  private def petDescr(pet: Pet) =
    <h4 class="description">
      {Constants(pet.tags.map(tagLink).toList: _*).map(_.bind)}
    </h4>

  @dom
  private def petPrice(pet: Pet) =
    <h3 class="red header">
      {pet.priceAsStr}
    </h3>

  @dom
  private def addToCardButton(pet: Pet) =
    <button class="ui fluid button">
      <i class="shopping cart icon large"></i>
      Add to Shopping Card
    </button>

  @dom
  private def petInfo(pet: Pet) = {
    <table class="ui very basic table">
      <tr>
        <td>Category:
          &nbsp;
        </td> <td>
        {categoryLink(pet.product.category).bind}
      </td>
      </tr>
      <tr>
        <td>Product:</td> <td>
        {productLink(pet.product).bind}
      </td>
      </tr>

    </table>
  }

  @dom
  private def imagePanel(pet: Pet) =
    <div>
      <img class="ui image" src={staticAsset(pet.firstPhotoUrl)}></img>
    </div>

}

object PetView {
  val hashRegex: Regex = """#pet/([^/]*)/([^/]*)/([^/]*)""".r

}
