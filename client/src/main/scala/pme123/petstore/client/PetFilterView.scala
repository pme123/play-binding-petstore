package pme123.petstore.client

import com.thoughtworks.binding.Binding.Constants
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.HTMLElement

import scala.util.matching.Regex

private[client] object PetFilterView
  extends PetTable {

  val hashRegex: Regex = """#filter""".r

  def name: String = "filter"

  val link: String = name

  // 1. level of abstraction
  // **************************
  @dom
  def create(): Binding[HTMLElement] = {
    <div class="">
      {//
      filterHeader.bind}{//
      filterDescr.bind}{//
      if(PetUIStore.uiState.pets.bind.isEmpty){
        <div class="content">
          <h3>No pets match your Filter.</h3>
        </div>
      } else petTable.bind}
    </div>
  }


  // 2. level of abstraction
  // **************************

  @dom
  private lazy val filterHeader = <h1 class="header">
    <i class={s"search big icon"}></i> &nbsp; &nbsp;
    Filter Results
  </h1>

  @dom
  private lazy val filterDescr = {
    val filter = UIFilter.filter.bind

    <div class="ui fluid card">
      <div class="content">
        <table class="">
          <tbody>
            <tr>
              <td>
                <b>Pet Description:</b>
              </td> <td>
              {filter.flatMap(_.petDescr).getOrElse("")}
            </td>
            </tr>
            <tr>
              <td>
                <b>Product:</b>
              </td> <td>
              {filter.flatMap(_.product).getOrElse("")}
            </td>
            </tr>
            <tr>
              <td>
                <b>Categories:</b>
              </td> <td>
              {Constants(filter.toSeq.flatMap(_.categories.map(tagLink)): _*).map(_.bind)}
            </td>
            </tr>
            <tr>
              <td>
                <b>Pet Tags:</b>
              </td> <td>
              {Constants(filter.toSeq.flatMap(_.petTags.map(tagLink)): _*).map(_.bind)}
            </td>
            </tr>
            <tr>
              <td>
                <b>Product Tags:
                  &nbsp;
                </b>
              </td> <td>
              {Constants(filter.toSeq.flatMap(_.productTags.map(tagLink)): _*).map(_.bind)}
            </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  }

}
