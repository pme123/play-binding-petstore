package pme123.petstore.shared

import play.api.libs.json.Json
import pme123.adapters.shared.ClientConfig
import pme123.petstore.shared.services.UnitTest

class PetTest extends UnitTest {

  val petProduct1 = PetProduct("CA-123", "Manx", PetCategory.Cats)
  val petProduct2 = PetProduct("CA-124", "Persian", PetCategory.Cats)


  val pet = Pet("CA-123", "Mausi", "A nice Supercat", 234, petProduct1)

  "Pet" should "be marshaled and un-marshaled correctly" in {

    Json.toJson(pet).validate[Pet].get should be(pet)
  }
}
