package pme123.adapters.server.entity

import java.io.InputStream

import org.apache.poi.ss.usermodel.WorkbookFactory
import pme123.petstore.server.entity.ImportWorkbook


class ImportWorkbookTest
  extends UnitTest {

  import ImportWorkbook._

  def badTestSFn: InputStream = getClass.getResourceAsStream("/excel/bad_excel.xlsx")

  lazy val badTestWorkbook = ImportWorkbook(WorkbookFactory.create(badTestSFn))

  def noConfigTestSFn: InputStream = getClass.getResourceAsStream("/excel/no_config_excel.xlsx")

  lazy val noConfigTestWorkbook = ImportWorkbook(WorkbookFactory.create(noConfigTestSFn))

  private val wb = workbook.wb
  workbook.printImport()
  "The PetStore import" should "be imported and contain 3 sheets" in {
    assert(wb.getNumberOfSheets === 3)
  }

  it should "contain a categories sheet" in {
    assert(wb.getSheet(sheetCategories) != null)
  }
  it should "contain a products sheet" in {
    assert(wb.getSheet(sheetPetProducts) != null)
  }
  it should "contain a pets sheet" in {
    assert(wb.getSheet(sheetPets) != null)
  }

  "The categories sheet" should "have 5 categrories" in {
    workbook.categories.get.length should be(5)
  }
  "The products sheet" should "have 4 products" in {
    workbook.petProducts.get.length should be(4)
  }
  "The pets sheet" should "have 3 pets" in {
    val value = workbook.pets.get
    value.length should be(3)
  }

  "The first Category" should "be" in {
    val category = workbook.categories.get.head.get
    category.ident should be("fish")
    category.name should be("Fish")
  }

  "The first Product" should "be" in {
    val product = workbook.petProducts.get.head.get
    product.productIdent should be("FIS-001")
    product.name should be("Angelfish")
    product.category.ident should be("fish")
    product.tags should be(Set("saltwater"))

  }

  "The first Pet" should "be" in {
    val pet = workbook.pets.get.head.get
    pet.itemIdent should be("ANG-0001")
    pet.descr should be("Large Angelfish")
    pet.product.name should be("Angelfish")
    pet.tags should be(Set())
  }

}
