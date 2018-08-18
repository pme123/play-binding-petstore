package pme123.petstore.server.control.auth

import com.google.inject.name.Named
import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api.actions.{SecuredErrorHandler, UnsecuredErrorHandler}
import com.mohiva.play.silhouette.api.crypto.{Crypter, CrypterAuthenticatorEncoder, Signer}
import com.mohiva.play.silhouette.api.services.{AuthenticatorService, IdentityService}
import com.mohiva.play.silhouette.api.util._
import com.mohiva.play.silhouette.api.{Environment, EventBus, Silhouette, SilhouetteProvider}
import com.mohiva.play.silhouette.crypto.{JcaCrypter, JcaCrypterSettings, JcaSigner, JcaSignerSettings}
import com.mohiva.play.silhouette.impl.authenticators.{CookieAuthenticator, CookieAuthenticatorService, CookieAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.util.{DefaultFingerprintGenerator, SecureRandomIDGenerator}
import com.mohiva.play.silhouette.password.BCryptPasswordHasher
import com.typesafe.config.Config
import net.codingwell.scalaguice.ScalaModule
import play.api.mvc.{Cookie, CookieHeaderEncoding}
import play.api.{ConfigLoader, Configuration}
import pme123.petstore.server.boundary.services.ErrorHandler
import pme123.petstore.server.entity.AuthUser

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

/**
  * Silhouette Module.
  * From example https://github.com/adrianhurt/play-silhouette-credentials-seed/tree/master/app/utils/silhouette
  */
class Module extends AbstractModule with ScalaModule {
  /**
    * @see https://www.playframework.com/documentation/2.6.x/ScalaDependencyInjection#programmatic-bindings
    */

  override def configure(): Unit = {

    bind[Silhouette[DefaultEnv]].to[SilhouetteProvider[DefaultEnv]]
    bind[SecuredErrorHandler].to[ErrorHandler]
    bind[UnsecuredErrorHandler].to[ErrorHandler]
    bind[IdentityService[AuthUser]].to[UserService]

    bind[IDGenerator].toInstance(new SecureRandomIDGenerator())
    bind[PasswordHasher].toInstance(new BCryptPasswordHasher)
    bind[FingerprintGenerator].toInstance(new DefaultFingerprintGenerator(false))
    bind[EventBus].toInstance(EventBus())
    bind[Clock].toInstance(Clock())
  }

  @Provides
  def provideEnvironment(userService: UserService,
                         authenticatorService: AuthenticatorService[CookieAuthenticator],
                         eventBus: EventBus): Environment[DefaultEnv] = {

    Environment[DefaultEnv](
      userService,
      authenticatorService,
      Seq(),
      eventBus
    )
  }

  @Provides
  def provideAuthenticatorService(@Named("authenticator-signer") signer: Signer,
                                  @Named("authenticator-crypter") crypter: Crypter,
                                  cookieHeaderEncoding: CookieHeaderEncoding,
                                  fingerprintGenerator: FingerprintGenerator,
                                  idGenerator: IDGenerator,
                                  configuration: Configuration,
                                  clock: Clock): AuthenticatorService[CookieAuthenticator] = {

    //config("security.cookieAuthenticator.cookieName")
    //
    val config = configuration.get[CookieAuthenticatorSettings]("security.cookieAuthenticator")
    val authenticatorEncoder = new CrypterAuthenticatorEncoder(crypter)
    new CookieAuthenticatorService(config, None, signer, cookieHeaderEncoding, authenticatorEncoder, fingerprintGenerator, idGenerator, clock)
  }

  @Provides
  @Named("authenticator-signer")
  def provideAuthenticatorSigner(configuration: Configuration): Signer = {
    val secretKey = configuration.get[String]("security.authenticator.secretKey")
    val secretSalt = configuration.get[String]("security.authenticator.secretKey")
    val config = JcaSignerSettings(secretKey, secretSalt)
    new JcaSigner(config)
  }

  @Provides
  @Named("authenticator-crypter")
  def provideAuthenticatorCrypter(configuration: Configuration): Crypter = {
    val secretKey = configuration.get[String]("security.authenticator.secretKey")
    val config = JcaCrypterSettings(secretKey)
    new JcaCrypter(config)
  }

  implicit val cookieAuthenticatorSettingsConfigLoader: ConfigLoader[CookieAuthenticatorSettings] = (rootConfig: Config, path: String) => {
    val config = Configuration(rootConfig.getConfig(path))
    CookieAuthenticatorSettings(
      cookieName = config.getOptional[String]("cookieName").getOrElse("id"),
      cookiePath = config.getOptional[String]("cookiePath").getOrElse("/"),
      cookieDomain = config.getOptional[String]("cookieDomain"),
      secureCookie = config.getOptional[Boolean]("secureCookie").getOrElse(true),
      httpOnlyCookie = config.getOptional[Boolean]("httpOnlyCookie").getOrElse(true),
      sameSite = config.getOptional[String]("sameSite").flatMap(Cookie.SameSite.parse),
      useFingerprinting = config.getOptional[Boolean]("useFingerprinting").getOrElse(true),
      cookieMaxAge = config.getOptional[FiniteDuration]("cookieMaxAge"),
      authenticatorIdleTimeout = config.getOptional[FiniteDuration]("authenticatorIdleTimeout"),
      authenticatorExpiry = config.getOptional[FiniteDuration]("authenticatorExpiry").getOrElse(12.hours)
    )
  }
}
