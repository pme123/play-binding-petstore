package pme123.petstore

import scala.language.implicitConversions

package object shared {

  class ExtString(val s: String) {
    def splitToSet: Set[String] = s.split(",").map(_.trim).filter(_.nonEmpty).toSet
  }

  implicit def stringToString(s: String): ExtString = new ExtString(s)
}
