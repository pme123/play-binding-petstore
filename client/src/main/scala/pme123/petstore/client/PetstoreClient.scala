package pme123.petstore.client

import pme123.petstore.client.services.SPAClient

import scala.language.implicitConversions
import scala.scalajs.js.annotation.JSExportTopLevel

object PetstoreClient
  extends SPAClient {

  // @JSExportTopLevel exposes this function with the defined name in Javascript.
  // this is called by the index.scala.html of the server.
  // the only connection that is not type-safe!
  @JSExportTopLevel("client.PetstoreClient.main")
  def main(context: String, webPath: String) {
    initClient(context, webPath)

    LandingPage().create()

  }

}


