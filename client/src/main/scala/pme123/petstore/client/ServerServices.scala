package pme123.petstore.client

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.client.services.{HttpServices, UIStore}
import pme123.petstore.shared._

import scala.scalajs.js

/**
  * Created by pascal.mengelt on 16.07.2017.
  */
object ServerServices
  extends HttpServices {

  def petCategories(): Binding[HTMLElement] = {
    val path = s"$apiPath/petCategories"

    httpGet(path, (results: PetCategories) => PetUIStore.changePetCategories(results))
  }

  def petProducts(petCategory: PetCategory): Binding[HTMLElement] = {
    val path = s"$apiPath/petProducts/${petCategory.ident}"

    httpGet(path, (results: PetProducts) => PetUIStore.changeAllPetProducts(results))
  }


  def pets(petProduct: PetProduct): Binding[HTMLElement] = {
    val path = s"$apiPath/pets/${petProduct.productIdent}"

    httpGet(path, (results: Pets) => PetUIStore.changePets(results))
  }

  def pet(petIdent: String): Binding[HTMLElement] = {
    val path = s"$apiPath/pet/$petIdent"

    httpGet(path, (result: Pet) => PetUIStore.changePet(result))
  }

  def petTags(): Binding[HTMLElement] = {
    val path = s"$apiPath/petTags"

    httpGet(path, (results: Seq[String]) => PetUIStore.changePetTags(results))
  }

  def productTags(): Binding[HTMLElement] = {
    val path = s"$apiPath/productTags"

    httpGet(path, (results: Seq[String]) => PetUIStore.changeProductTags(results))
  }

  @dom
  val runFilter: Binding[HTMLElement] = {
    val filter = UIFilter.filter.bind
    if (filter.isDefined) {
      UIRoute.changeRoute(PetFilterView)
    }
    val path = s"$apiPath/filter"
    <div>
      {httpPut(path, filter, (results: List[Pet]) => PetUIStore.changePets(results)).bind}
    </div>

  }

}
