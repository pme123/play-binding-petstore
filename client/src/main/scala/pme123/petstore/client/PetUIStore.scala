package pme123.petstore.client

import com.thoughtworks.binding.Binding.{Var, Vars}
import pme123.petstore.shared._
import pme123.petstore.shared.services.Logging

import scala.language.implicitConversions

object PetUIStore extends Logging {

  val uiState = UIState()

  def changePetCategory(id: String): PetCategory = {
    info(s"UIStore: changePetCategory $id")
    uiState.petCategory.value = PetCategory.withNameInsensitive(id)
    uiState.petCategory.value
  }

  def changePetCategory(petCategory: PetCategory): PetCategory = {
    info(s"UIStore: changePetCategory $petCategory")
    uiState.petCategory.value = petCategory
    petCategory
  }

  def changePetProduct(productIdent: String): Option[PetProduct] = {
    info(s"UIStore: changePetProduct $productIdent")
    val maybeProduct = uiState.petProductsFor(uiState.petCategory.value).value
      .find(_.productIdent == productIdent)
    uiState.petProduct.value = maybeProduct
    maybeProduct
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

  def changeAllPetProducts(petProducts: PetProducts): PetProducts = {
    info(s"UIStore: changeAllPetProducts $petProducts")
    val prods: Vars[PetProduct] = uiState.allPetProducts.value(petProducts.category)
    prods.value.clear()
    prods.value ++= petProducts.products
    petProducts
  }

  def changePets(pets: Pets): Pets = {
    Pets(pets.petProduct, changePets(pets.pets))
  }

  def changePets(pets: List[Pet]): List[Pet] = {
    info(s"UIStore: changePets $pets")
    uiState.pets.value.clear()
    uiState.pets.value ++= pets
    pets
  }

  def changePetTags(tags: Seq[String]): Seq[String] = {
    info(s"UIStore: changePetTags $tags")
    uiState.petTags.value.clear()
    uiState.petTags.value ++= tags
    tags
  }

  def changeProductTags(tags: Seq[String]): Seq[String] = {
    info(s"UIStore: changeProductTags $tags")
    uiState.productTags.value.clear()
    uiState.productTags.value ++= tags
    tags
  }

  // make sure all are closed
  private def hideAllDialogs(): Unit = {

  }


  case class UIState(
                      petCategory: Var[PetCategory] = Var(PetCategory.Dogs),
                      petProduct: Var[Option[PetProduct]] = Var(None),
                      allPetProducts: Var[Map[PetCategory, Vars[PetProduct]]] = UIState.initAllPetProducts(),
                      petTags: Vars[String] = Vars(),
                      productTags: Vars[String] = Vars(),
                      pets: Vars[Pet] = Vars()
                    ) {

    def petProductsFor(petCategory: PetCategory): Vars[PetProduct] =
      allPetProducts.value(petCategory)

  }

  object UIState {
    def initAllPetProducts(): Var[Map[PetCategory, Vars[PetProduct]]] = {
      Var(PetCategory.values.map(_ -> Vars.empty[PetProduct]).toMap)
    }
  }

}
