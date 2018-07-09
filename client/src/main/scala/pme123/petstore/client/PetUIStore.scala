package pme123.petstore.client

import com.thoughtworks.binding.Binding.{Var, Vars}
import pme123.petstore.shared._
import pme123.petstore.shared.services.Logging

import scala.language.implicitConversions

object PetUIStore extends Logging {

  val uiState = UIState()

  def changePetCategory(petCategory: PetCategory): PetCategory = {
    info(s"UIStore: changePetCategory $petCategory")
    uiState.petCategory.value = petCategory
    petCategory
  }

  def changePetCategories(petCategories: PetCategories): PetCategories = {
    info(s"UIStore: changePetCategories $petCategories")
    uiState.petCategories.value.clear()
    uiState.petCategories.value ++= petCategories.categories
    petCategories
  }

  def changePetProducts(petProducts: PetProducts): PetProducts = {
    info(s"UIStore: changePetProducts $petProducts")
    uiState.petProducts.value.clear()
    uiState.petProducts.value ++= petProducts.products
    petProducts
  }

  def changePets(pets: Pets): Pets = {
    info(s"UIStore: changePets $pets")
    uiState.pets.value.clear()
    uiState.pets.value ++= pets.pets
    pets
  }

  // make sure all are closed
  private def hideAllDialogs(): Unit = {

  }


  case class UIState(
                      petCategories: Vars[PetCategory] = Vars(),
                      petCategory: Var[PetCategory] = Var(PetCategory.Dogs),
                      petProducts: Vars[PetProduct] = Vars(),
                      pets: Vars[Pet] = Vars()
                    )

}
