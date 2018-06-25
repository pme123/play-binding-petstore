package pme123.petstore.server.entity

import java.time.{Instant, ZoneId}

import play.api.Configuration
import pme123.petstore.shared.services.{Logging, SettingsProp}

import scala.language.implicitConversions

/**
  * created by pascal.mengelt
  * This config uses the small framework typesafe-config.
  * See here the explanation: https://github.com/typesafehub/config
  * The configuration can be overridden direct in distribution.
  * see here the base: http://stackoverflow.com/questions/6201349/how-to-read-properties-file-placed-outside-war
  *
  * The defaults are described in reference.conf
  */
object PetConfSettings {
  val configPath = "pme123.petstore"

  // env
  val runModeProp = "run.mode"
  val charEncodingProp = "char.encoding"
  val timezoneProp = "timezone"
  val wsocketHostsAllowedProp = "wsocket.hosts.allowed"

  // other
  val httpContextProp = "play.http.context"

}

// this settings will be validated on startup
abstract class PetConfSettings(config: Configuration)
  extends SettingsPropsImplicits {

  import PetConfSettings._

  val name = "petstore"

  private val baseConfig: Configuration = config.get[Configuration](configPath)

  // env
  val runMode: String = baseConfig.get[String](runModeProp)
  val charEncoding: String = baseConfig.get[String](charEncodingProp)
  val timezone: String = baseConfig.get[String](timezoneProp)
  val timezoneID: ZoneId = ZoneId.of(timezone)
  val wsocketHostsAllowed: Seq[String] = baseConfig.get[Seq[String]](wsocketHostsAllowedProp)

  // other
  val httpContext: String = config.get[String](httpContextProp)

  lazy val props: Seq[SettingsProp] = {
    Seq(
      //api
      SettingsProp(runModeProp, runMode)
      , SettingsProp(timezoneProp, timezone)
      , SettingsProp(charEncodingProp, charEncoding)
      , SettingsProp(wsocketHostsAllowedProp, wsocketHostsAllowed)

      // other
      , SettingsProp(httpContextProp, httpContext)

    )
  }


}


trait SettingsPropsImplicits
  extends Logging {
  def name: String

  def props: Seq[SettingsProp]

  protected def pwd(value: String): String = "*" * value.length

  lazy val asString: String = props.map(_.asString(name)).mkString("\n")

  implicit def fromAny(bool: Boolean): String = bool.toString

  implicit def fromInt(nbr: Int): String = nbr.toString

  implicit def fromInstant(inst: Instant): String = inst.toString

  implicit def fromOption(opt: Option[_]): String = opt.map(_.toString).getOrElse("-")

  implicit def fromSeq(seq: Seq[String]): String = seq.mkString("[", ",", "]")

}