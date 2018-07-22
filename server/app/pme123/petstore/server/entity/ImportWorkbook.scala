package pme123.petstore.server.entity

import org.apache.poi.ss.usermodel._
import pme123.petstore.shared._
import pme123.petstore.shared.services.{Logging, SPAException}

import scala.collection.JavaConverters._
import scala.util._

/**
  * Class that encapsulates the SFn file.
  * The SFn is specified here: https://screenfood.atlassian.net/wiki/x/GYDNAQ
  * Created by pascal.mengelt on 10.04.2015.
  */
case class ImportWorkbook(wb: Workbook)
  extends Logging {

  import ImportWorkbook._

  private val sheetNotAvailable = "There is no SFn-Sheet with the name: "
  private val contentNotAvailable = "There is no Content with the name: "

  def printImport() {

    info(s"Imported sfn with ${wb.getNumberOfSheets} sheets.")
    for {
      sheetIndex: Int <- 0 until wb.getNumberOfSheets
      sheet = wb.getSheetAt(sheetIndex)
    } printSheet(sheet)

    def printSheet(sheet: Sheet) {
      info("*" * 25)
      info("Sheet: ${sheet.getSheetName}")
      for (row <- sheet.rowIterator().asScala) printRow(row)

      def printRow(row: Row) = {
        val rowStrs = for (cell <- row.cellIterator().asScala) yield cellAsString(cell)
        info(rowStrs.mkString("\t: "))
      }

    }
  }

  lazy val categories: Try[Seq[Try[PetCategory]]] =
    getSheet(sheetCategories) map { s =>
      rows(s).map { row =>
        Try(
          PetCategory(cellAsString(row.getCell(col0)),
            cellAsString(row.getCell(col1)),
            cellAsString(row.getCell(col2)))
        ) recoverWith failure(sheetCategories, row.getRowNum)
      }
    }

  lazy val petProducts: Try[Seq[Try[PetProduct]]] =
    getSheet(sheetPetProducts) map { s =>
      rows(s).map { row =>
        Try(
          PetProduct(cellAsString(row.getCell(col0)),
            cellAsString(row.getCell(col1)),
            findCategory(cellAsString(row.getCell(col2))).get,
            cellAsStringSet(row.getCell(col3)))
        ) recoverWith failure(sheetPetProducts, row.getRowNum)
      }
    }

  lazy val pets: Try[Seq[Try[Pet]]] = {
    getSheet(sheetPets) map { s =>
      rows(s).map { row =>
        Try(
          Pet(cellAsString(row.getCell(col0)),
            cellAsString(row.getCell(col1)),
            cellAsDouble(row.getCell(col2)),
            findProduct(cellAsString(row.getCell(col3))).get,
            PetStatus.withNameInsensitive(cellAsString(row.getCell(col4))),
            cellAsStringSet(row.getCell(col5)),
            cellAsStringSet(row.getCell(col6)))
        ) recoverWith failure(sheetPets, row.getRowNum)
      }
    }
  }

  private def getSheet(sheetName: String): Try[Sheet] = {
    val sheet = wb.getSheet(sheetName)
    if (Option(sheet).isEmpty) {
      Failure(new IllegalArgumentException(s"$sheetNotAvailable $sheetName"))
    } else {
      Success(sheet)
    }
  }

  private def findContent[T <: Identifiable](name: String, contents: Try[Seq[Try[T]]]): Try[T] = {
    findContent(name, contents, (c: Try[T]) => c.isSuccess && c.get.ident == name)
  }

  private def findContent[T](name: String, contents: Try[Seq[Try[T]]], filter: Try[T] => Boolean): Try[T] = {
    contents flatMap (cs => (cs.filter(c => filter(c)).toList match {
      case Nil => Failure(new IllegalArgumentException(s"$contentNotAvailable $name"))
      case x :: _ => Success(x)
    }).flatten)
  }

  private def findCategory(name: String): Try[PetCategory] = findContent(name, categories)

  private def findProduct(name: String): Try[PetProduct] = findContent(name, petProducts)

  private def failure[T](sheetName: String, rowNr: Int): PartialFunction[Throwable, Try[T]] = {
    case exc: Exception => Failure(BadRowException(sheetName, rowNr, Some(exc)))
  }

  private def rows(sheet: Sheet): Seq[Row] = for {
    index <- 1 to sheet.getLastRowNum
    row = sheet.getRow(index)
    if Option(row).isDefined
  } yield row

  private def cellAsString(cell: Cell) = {
    if (Option(cell).isEmpty) {
      ""
    } else {
      cell.getCellTypeEnum match {
        case CellType.NUMERIC =>
          new DataFormatter().formatCellValue(cell)
        case CellType.STRING =>
          cell.getStringCellValue
        case CellType.FORMULA =>
          val evaluator = wb.getCreationHelper.createFormulaEvaluator()
          new DataFormatter().formatCellValue(cell, evaluator)
        case _ => ""
      }
    }
  }

  private def cellAsStringSet(cell: Cell) =
    cellAsString(cell).split(",").map(_.trim).filter(_.nonEmpty).toSet

  private def cellAsDouble(cell: Cell) = cellAsString(cell).toDouble

}

object ImportWorkbook {
  val sheetConfig = "config"
  val sheetCategories = "categories"
  val sheetPetProducts = "products"
  val sheetPets = "pets"


  val col0 = 0
  val col1 = 1
  val col2 = 2
  val col3 = 3
  val col4 = 4
  val col5 = 5
  val col6 = 6
  val col7 = 7
  val col8 = 8
  val col9 = 9
  val col10 = 10
  val col11 = 11
  val col12 = 12

  private lazy val excel = getClass.getResourceAsStream("/excel/excel.xlsx")

  lazy val workbook = ImportWorkbook(WorkbookFactory.create(excel))
}

case class ExcelImportException(msg: String) extends SPAException

case class ExcelExportException(msg: String) extends SPAException

object NoConfigException extends SPAException {
  val msg = "There is no Configuration Sheet ('config')!"
}

case class BadRowException(sheetName: String, rowNr: Int, override val cause: Option[Throwable]) extends SPAException {
  val msg = s"There was an Exception on row $rowNr of sheet $sheetName! Message: ${cause.map(_.getMessage).getOrElse('-')}"
}
