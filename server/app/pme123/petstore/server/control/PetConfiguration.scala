package pme123.petstore.server.control

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import pme123.petstore.server.entity.PetConfSettings

@Singleton
class PetConfiguration @Inject()(conf: Configuration) extends PetConfSettings(conf){

}
