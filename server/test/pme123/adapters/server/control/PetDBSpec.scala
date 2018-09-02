package pme123.adapters.server.control

import org.scalatest.TestSuite
import pme123.petstore.server.control.PetDBRepo

class PetDBSpec
  extends GuiceAcceptanceSpec {

  private lazy val petDBRepo = inject[PetDBRepo]

  "PetDB" should {

    "initialize the Category Table" in {
      petDBRepo.selectCategories()
        .map(categories =>
          assert(categories.size >= 5))
    }
    "initialize the Product Table" in {
      petDBRepo.selectProducts()
        .map(products =>
          assert(products.size >= 15))
    }
    "initialize the Pet Table" in {
      petDBRepo.selectPets()
        .map(pets =>
          assert(pets.size >= 15))
    }

  }
}


