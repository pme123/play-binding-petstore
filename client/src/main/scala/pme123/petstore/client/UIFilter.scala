package pme123.petstore.client

import com.thoughtworks.binding.Binding.Var
import pme123.petstore.shared.PetFilter
import pme123.petstore.shared.services.Logging


case class UIFilter(categories: Option[String] = None) {
  val query: String = categories.map(c => s"categories=$c").toSeq.mkString("&")

}

object UIFilter extends Logging {

  val filter: Var[Option[PetFilter]] = Var(None)

  def changePetDescr(petDescr: String): Option[PetFilter] = {
    info(s"UIFilter: changePetDescr $petDescr")
    val exText = filter.value.flatMap(_ .petDescr)
    val text: Option[String] = Some(petDescr).filter(_.length > 2)
    if(exText != text){
      val exFilter = filter.value.getOrElse(PetFilter())
      filter.value = Some(exFilter.withPetDescr(text))
    }
    filter.value
  }

  def changeProduct(product: String): Option[PetFilter] = {
    info(s"UIFilter: changeProduct $product")
    val exText = filter.value.flatMap(_ .product)
    val text: Option[String] = Some(product).filter(_.length > 2)
    if(exText != text){
      val exFilter = filter.value.getOrElse(PetFilter())
      filter.value = Some(exFilter.withProduct(text))
    }
    filter.value
  }

  def changeCategories(categories: String): Option[PetFilter] = {
    info(s"UIFilter: changeCategories $categories")

    val exFilter = filter.value.getOrElse(PetFilter())
    filter.value = Some(exFilter.withCategories(categories))
    filter.value
  }

  def changePetTags(tags: String): Option[PetFilter] = {
    info(s"UIFilter: changePetTags $tags")

    val exFilter = filter.value.getOrElse(PetFilter())
    filter.value = Some(exFilter.withPetTags(tags))
    filter.value
  }

  def changeProductTags(tags: String): Option[PetFilter] = {
    info(s"UIFilter: changeProductTags $tags")

    val exFilter = filter.value.getOrElse(PetFilter())
    filter.value = Some(exFilter.withProductTags(tags))
    filter.value
  }
}
