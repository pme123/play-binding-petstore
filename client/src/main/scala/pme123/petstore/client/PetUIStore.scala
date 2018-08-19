package pme123.petstore.client

import com.thoughtworks.binding.Binding.{Var, Vars}
import pme123.petstore.shared._
import pme123.petstore.shared.services.Logging

import scala.language.implicitConversions

object PetUIStore extends Logging {

  val maxPathMsgs = 200

  val uiState = UIState()

  def changePetCategory(ident: String): PetCategory =
    changePetCategory(uiState.petCategoryFor(ident))

  def changePetCategory(petCategory: PetCategory): PetCategory = {
    info(s"UIStore: changePetCategory $petCategory")
    uiState.petCategory.value = Some(petCategory)
    petCategory
  }

  def changePetProduct(productIdent: String): Option[PetProduct] = {
    info(s"UIStore: changePetProduct $productIdent - ${uiState.petCategory.value} - ${uiState.petProductsFor("mice").value}")
    val maybeProduct = uiState.petCategory.value.toSeq
      .flatMap(cat => uiState.petProductsFor(cat.ident).value)
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

  def changePet(pet: Pet): UIPet = {
    info(s"UIStore: changePet $pet")
    uiState.pet.value = UIPet(Some(pet))
    uiState.pet.value
  }

  def changePetCategories(petCategories: PetCategories): PetCategories = {
    info(s"UIStore: changePetCategories $petCategories")
    uiState.petCategories.value.clear()
    uiState.petCategories.value ++= petCategories.categories
    petCategories
  }

  def changeAllPetProducts(petProducts: PetProducts): PetProducts = {
    info(s"UIStore: changeAllPetProducts $petProducts")
    val prods: Vars[PetProduct] = uiState.petProductsFor(petProducts.category.ident)
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

  def addPathMsg(pathMsg: PathMsg) {
    info(s"UIStore: addPathMsg $pathMsg")
    uiState.pathMsgs.value += pathMsg
    restrictPathMsgs()
  }

  private def restrictPathMsgs() {
    if (uiState.pathMsgs.value.length > maxPathMsgs)
      uiState.pathMsgs.value.remove(0, uiState.pathMsgs.value.length - maxPathMsgs)
  }

  case class UIState(petCategory: Var[Option[PetCategory]] = Var(None),
                     petProduct: Var[Option[PetProduct]] = Var(None),
                     pet: Var[UIPet] = Var(UIPet()),
                     petTags: Vars[String] = Vars(),
                     productTags: Vars[String] = Vars(),
                     petCategories: Vars[PetCategory] = Vars(),
                     pets: Vars[Pet] = Vars(),
                     pathMsgs: Vars[PathMsg] = Vars()
                    ) {


    private lazy val allPetProducts: Var[Map[String, Vars[PetProduct]]] = Var(Map())

    def petProductsFor(petCategory: PetCategory): Vars[PetProduct] =
      petProductsFor(petCategory.ident)

    def petProductsFor(categoryIdent: String): Vars[PetProduct] = {
      allPetProducts.value.getOrElse(categoryIdent, {
        val petProducts = Vars.empty[PetProduct]
        allPetProducts.value = allPetProducts.value.updated(categoryIdent, petProducts)
        petProducts
      })
    }

    def petCategoryFor(ident: String): PetCategory =
      petCategories.value.find(_.ident == ident)
        .get

  }

}

case class UIPet(maybePet: Option[Pet] = None) {

  val nonEmpty: Boolean = maybePet.nonEmpty
  lazy val pet: Pet = maybePet.get
}
