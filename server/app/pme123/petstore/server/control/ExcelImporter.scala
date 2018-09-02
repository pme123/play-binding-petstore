package pme123.petstore.server.control

import javax.inject.{Inject, Singleton}
import pme123.petstore.server.control.services.UserDBRepo
import pme123.petstore.shared.services.Logging

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

@Singleton
class ExcelImporter @Inject()(petDBInitializer: PetDBInitializer,
                              userDBRepo: UserDBRepo,
                              petDBRepo: PetDBRepo) // to make sure it is initialized
                             (implicit val ec: ExecutionContext)
  extends Logging {

  import pme123.petstore.server.entity.ImportWorkbook._

  importCategories
  importProducts
  importPets
  importUsers

  private def importCategories = {
    workbook.categories match {
      case Success(categories) =>
        categories.map {
          case Success(cat) => petDBRepo.insertCategory(cat)
          case Failure(exc) => error(exc)
        }
      case Failure(exc) => error(exc)
    }
  }

  private def importProducts = {
    workbook.petProducts match {
      case Success(petProducts) =>
        petProducts.map {
          case Success(prod) => petDBRepo.insertProduct(prod)
          case Failure(exc) => error(exc)
        }
      case Failure(exc) => error(exc)
    }
  }

  private def importPets = {
    workbook.pets match {
      case Success(pets) =>
        pets.map {
          case Success(pet) => petDBRepo.insertPet(pet)
          case Failure(exc) => error(exc)
        }
      case Failure(exc) => error(exc)
    }
  }

  private def importUsers = {
    workbook.users match {
      case Success(users) =>
        users.map {
          case Success(user) => userDBRepo.insertUser(user)
          case Failure(exc) => error(exc)
        }
      case Failure(exc) => error(exc)
    }
  }
}
