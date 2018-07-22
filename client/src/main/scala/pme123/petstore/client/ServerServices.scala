package pme123.petstore.client

import com.thoughtworks.binding.{Binding, dom}
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
    val apiPath = s"${UIStore.uiState.webContext.value}/api/petProducts/${petCategory.ident}"

    httpGet(apiPath, (results: PetProducts) => PetUIStore.changeAllPetProducts(results))
  }


  def pets(petProduct: PetProduct): Binding[HTMLElement] = {
    val apiPath = s"${UIStore.uiState.webContext.value}/api/pets/${petProduct.productIdent}"

    httpGet(apiPath, (results: Pets) => PetUIStore.changePets(results))
  }

  def pet(petIdent: String): Binding[HTMLElement] = {
    val apiPath = s"${UIStore.uiState.webContext.value}/api/pet/$petIdent"

    httpGet(apiPath, (result: Pet) => PetUIStore.changePet(result))
  }

  def petTags(): Binding[HTMLElement] = {
    val apiPath = s"${UIStore.uiState.webContext.value}/api/petTags"

    httpGet(apiPath, (results: Seq[String]) => PetUIStore.changePetTags(results))
  }

  def productTags(): Binding[HTMLElement] = {
    val apiPath = s"${UIStore.uiState.webContext.value}/api/productTags"

    httpGet(apiPath, (results: Seq[String]) => PetUIStore.changeProductTags(results))
  }

  @dom
  val runFilter: Binding[HTMLElement] = {
    val filter = UIFilter.filter.bind
    if (filter.isDefined) {
      UIRoute.changeRoute(PetFilterView)
    }
    val apiPath = s"${UIStore.uiState.webContext.value}/api/filter"
    <div>
      {httpPut(apiPath, filter, (results: List[Pet]) => PetUIStore.changePets(results)).bind}
    </div>

  }

}
