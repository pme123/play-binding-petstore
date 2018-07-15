
import scala.util.matching.Regex

val hashRegex: Regex = """#product/([^/]*)/([^/]*)""".r

def createView(hashText: String) = hashText match {
  case hashRegex(idCat, id) =>
    "ok"
  case _ =>
    "not ok"
}

createView("#product/Birds/BIR-001")

val price = 12.5
f"$price%.2f"
