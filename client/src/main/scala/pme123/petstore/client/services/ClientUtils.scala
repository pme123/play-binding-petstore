package pme123.petstore.client.services

import java.time.Instant

import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement
import pme123.petstore.shared.services.Logging

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}

trait ClientUtils
  extends IntellijImplicits
    with Logging {

  def jsLocalDateTime(instant: Instant): String = {
    val date = new js.Date(1000.0 * instant.getEpochSecond)

    s"${date.toLocaleDateString} ${date.toLocaleTimeString}"
  }

  // helper - need separate class at some point
  def jsLocalDate(dateString: String): String = {
    val date = new js.Date(dateString)
    date.toLocaleDateString()
  }

  def jsLocalTime(dateString: String): String = {
    val date = new js.Date(dateString)
    if (date.getHours() == 0)
      ""
    else {
      val timeStr = date.toLocaleTimeString
      timeStr.replaceFirst(":\\d\\d\\b", "") // remove seconds
    }
  }

  // get Assets from the servers /public folder
  def staticAsset(path: String): String =
    "" + g.jsRoutes.controllers.Assets.versioned(path).url

  @dom
  def faviconElem: Binding[HTMLElement] =
    <div class="ui item">
      <img src={staticAsset("images/favicon.png")}></img>
    </div>

  @dom
  def td(value: String): Binding[HTMLElement] =
    <td>
      {value}
    </td>

  @dom
  def tdImg(imageUrl: String): Binding[HTMLElement] =
    <td>
      <img class="defaultImage" src={imageUrl}/>
    </td>

  @dom
  def loadingElem: Binding[HTMLElement] =
    <div class="ui active inverted dimmer front">
      <div class="ui large text loader">Loading</div>
    </div>

  @dom
  def tagLink(tag: String): Binding[HTMLElement] =
    <div class="ui tag label">
      {tag}
    </div>

  def activeStyle(isActive:Boolean):String = if(isActive) "active blue" else ""

}

trait IntellijImplicits {
  //noinspection NotImplementedCode
  implicit def makeIntellijHappy(x: scala.xml.Elem): Binding[HTMLElement] = ???
}
