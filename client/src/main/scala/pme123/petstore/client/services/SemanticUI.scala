package pme123.petstore.client.services

import org.scalajs.jquery.JQuery

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.ScalaJSDefined

/**
  *
  */
object SemanticUI {

  // Monkey patching JQuery
  @js.native
  trait SemanticJQuery extends JQuery {
    def dropdown(params: js.Any*): SemanticJQuery = js.native

    def popup(params: js.Any*): SemanticJQuery = js.native

    def modal(params: js.Any*): SemanticJQuery = js.native

    def checkbox(params: js.Any*): SemanticJQuery = js.native

    def form(params: js.Any*): SemanticJQuery = js.native

  }

  // Monkey patching JQuery with implicit conversion
  implicit def jq2semantic(jq: JQuery): SemanticJQuery = jq.asInstanceOf[SemanticJQuery]

  @ScalaJSDefined
  trait Form extends js.Object {
    def fields: js.Object
  }

  @ScalaJSDefined
  trait Field extends js.Object {
    def identifier: String

    def rules: js.Array[Rule]
  }

  @ScalaJSDefined
  trait Rule extends js.Object {
    def `type`: String

    def prompt: js.UndefOr[String] = js.undefined
  }

  def columnWide(wide: Int): String = {
    wide match {
      case 1 => "one"
      case 2 => "two"
      case 3 => "three"
      case 4 => "four"
      case 5 => "five"
      case 6 => "six"
      case 7 => "seven"
      case 8 => "eight"
      case 9 => "nine"
      case 10 => "ten"
      case 11 => "eleven"
      case 12 => "twelve"
      case 13 => "thirteen"
      case 14 => "fourteen"
      case 15 => "fifteen"
      case _ => "sixteen"
    }
  }
}