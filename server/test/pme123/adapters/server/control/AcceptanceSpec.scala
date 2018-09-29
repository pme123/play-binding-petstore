package pme123.adapters.server.control

import org.scalatest._
import org.scalatestplus.play.WsScalaTestClient
import pme123.petstore.shared.services.Logging

/**
  * General Test Definition for ScalaTests
  */
trait AcceptanceSpec
  extends WordSpec
   // with AsyncTestSuite
   // with TestSuite
    with MustMatchers
    with OptionValues
    with WsScalaTestClient
    with Logging {
}
