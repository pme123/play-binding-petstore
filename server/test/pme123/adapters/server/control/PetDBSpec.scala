package pme123.adapters.server.control

import pme123.petstore.server.control.PetDBRepo
import pme123.petstore.shared.{Pet, PetCategory, PetProduct, PetStatus}

class PetDBSpec
  extends GuiceAcceptanceSpec
    with PetDBRepo {

  "PetDB" should {
    val category = PetCategory("testCat", "Test Cat", "Some Sub Title")
    val product = PetProduct("testProd", "Test Product", category, Set("female", "exotic"))

    "initialize the Category Table" in {
      val categories = selectCategories()
      assert(categories.size >= 5)
    }
    "initialize the Product Table" in {
      val products = selectProducts()
      assert(products.size >= 15)
    }
    "initialize the Pet Table" in {
      val pets = selectPets()
      assert(pets.size >= 15)
    }

  }
}


