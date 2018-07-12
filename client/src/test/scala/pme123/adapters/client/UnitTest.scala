package pme123.adapters.client

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FlatSpec, Matchers}
import pme123.petstore.shared.services.Logging

trait UnitTest
  extends FlatSpec
      with Matchers
      with BeforeAndAfter
      with BeforeAndAfterAll
      with Logging {

  }
