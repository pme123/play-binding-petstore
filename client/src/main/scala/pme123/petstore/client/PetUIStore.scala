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

  def changePetProduct(petProduct: PetProduct): PetProduct = {
    info(s"UIStore: changePetProduct $petProduct")
    uiState.petProduct.value = Some(petProduct)
    petProduct
  }

  def clearPetProduct() {
    info(s"UIStore: clearPetProduct")
    uiState.petProduct.value = None
  }

  def changePetProducts(petCategory: PetCategory): Seq[PetProduct] = {
    info(s"UIStore: changePetProducts to $petCategory")
    uiState.petProducts.value.clear()
    val petProducts = uiState.allPetProducts.value.getOrElse(petCategory, Nil)
    uiState.petProducts.value ++= petProducts
    petProducts
  }

  def changeAllPetProducts(petProducts: PetProducts): PetProducts = {
    info(s"UIStore: changeAllPetProducts $petProducts")
    uiState.allPetProducts.value = uiState.allPetProducts.value.updated(petProducts.category, petProducts.products)
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
                      petProduct: Var[Option[PetProduct]] = Var(None),
                      petProducts: Vars[PetProduct] = Vars(),
                      allPetProducts: Var[Map[PetCategory, Seq[PetProduct]]] = Var(Map()),
                      pets: Vars[Pet] = Vars()
                    ) {

    def petProductsFor(petCategory: PetCategory) =
      allPetProducts.value.getOrElse(petCategory, Nil)
  }
}
