package pme123.adapters.server.control

import akka.util.Timeout
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.ws.WSClient

import scala.concurrent.duration._
import scala.reflect.ClassTag

/**
  * Created by pascal.mengelt on 20.10.2016.
  */
trait GuiceAcceptanceSpec
  extends AcceptanceSpec
    // if you want to test only one Test you need:
    with GuiceOneAppPerSuite {

  implicit val timeout: Timeout = Timeout(2.second)

  implicit lazy val wsClient: WSClient = inject[WSClient]

  def inject[A](implicit tag: ClassTag[A]): A =
    app.injector.instanceOf(tag)
}
