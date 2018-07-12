package pme123.petstore.client

import com.thoughtworks.binding.Binding
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.client.services.{HttpServices, UIStore}
import pme123.petstore.shared._

/**
  * Created by pascal.mengelt on 16.07.2017.
  */
object ServerServices
  extends HttpServices {

  def petCategories(): Binding[HTMLElement] = {
    val apiPath = s"${UIStore.uiState.webContext.value}/api/petCategories"

    httpGet(apiPath, (results: PetCategories) => PetUIStore.changePetCategories(results))
  }

  def petProducts(petCategory: PetCategory): Binding[HTMLElement] = {
    val apiPath = s"${UIStore.uiState.webContext.value}/api/petProducts/${petCategory.entryName}"

    httpGet(apiPath, (results: PetProducts) => PetUIStore.changeAllPetProducts(results))
  }

  def pets(petProduct: PetProduct): Binding[HTMLElement] = {
    val apiPath = s"${UIStore.uiState.webContext.value}/api/pets/${petProduct.productIdent}"

    httpGet(apiPath, (results: Pets) => PetUIStore.changePets(results))
  }


}
