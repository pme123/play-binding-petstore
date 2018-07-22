package pme123.petstore.shared

import play.api.libs.json.Json
import pme123.petstore.shared.services.UnitTest

class PetTest extends UnitTest {

  private val category = PetCategory("cats", "Cats", "Various Breeds")
  val petProduct1 = PetProduct("CA-123", "Manx", category)
  val petProduct2 = PetProduct("CA-124", "Persian", category)


  val pet = Pet("CA-123", "A nice Supercat", 12.5, petProduct1)

  "Pet" should "be marshaled and un-marshaled correctly" in {

    Json.toJson(pet).validate[Pet].get should be(pet)
  }
}
