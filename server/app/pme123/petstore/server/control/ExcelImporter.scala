package pme123.petstore.server.control

import javax.inject.{Inject, Singleton}
import pme123.petstore.shared.services.Logging

import scala.util.{Failure, Success}

@Singleton
class ExcelImporter @Inject()(petDBInitializer: PetDBInitializer) // to make sure it is initialized
  extends PetDBRepo
    with Logging {

  import pme123.petstore.server.entity.ImportWorkbook._

  importCategories
  importProducts
  importPets

  private def importCategories = {
    workbook.categories match {
      case Success(categories) =>
        categories.map {
          case Success(cat) => insertCategory(cat)
          case Failure(exc) => error(exc)
        }
      case Failure(exc) => error(exc)
    }
  }

  private def importProducts = {
    workbook.petProducts match {
      case Success(petProducts) =>
        petProducts.map {
          case Success(prod) => insertProduct(prod)
          case Failure(exc) => error(exc)
        }
      case Failure(exc) => error(exc)
    }
  }

  private def importPets = {
    workbook.pets match {
      case Success(pets) =>
        pets.map {
          case Success(pet) => insertPet(pet)
          case Failure(exc) => error(exc)
        }
      case Failure(exc) => error(exc)
    }
  }
}
