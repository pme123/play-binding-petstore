package pme123.adapters.server.control.services

import pme123.adapters.server.control.GuiceAcceptanceSpec
import pme123.petstore.server.control.services.UserDBRepo

class UserDBSpec
  extends GuiceAcceptanceSpec {

  private lazy val userDBRepo = inject[UserDBRepo]

  "UserDBSpec" should {
    "initialize the User Table" in {
      userDBRepo.selectUsers()
        .map(users =>
          assert(users.size >= 3))
    }

  }
}


