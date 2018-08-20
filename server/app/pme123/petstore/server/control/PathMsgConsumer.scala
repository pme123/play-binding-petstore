package pme123.petstore.server.control

import akka.NotUsed
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink}
import javax.inject.Inject
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import play.api.libs.json.{JsValue, Json}
import pme123.petstore.server.boundary.services.SPAComponents
import pme123.petstore.shared.{PathMsg, PetWebSocketMsg}
import pme123.petstore.shared.services.Logging
import pme123.petstore.shared.services.User.UserId

import scala.concurrent.{ExecutionContext, Future}

class PathMsgConsumer @Inject()(comps: SPAComponents)
                               (implicit val ec: ExecutionContext,
                                mat: Materializer)
  extends Logging {

  /**
    * Creates a Future containing a Flow of JsValue in and out.
    */
  def consumerFlow(userId: UserId): Future[Flow[JsValue, JsValue, NotUsed]] = {
    Future.successful(
      Flow.fromSinkAndSource(in, out(userId)))
  }

  private val kafkaConfig = comps.config.kafkaWsPathMsgConsumer.underlying

  private def consumerSettings(userId: UserId) =
    ConsumerSettings(kafkaConfig, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(comps.config.kafkaWsBootstrapServers)
      .withGroupId(userId)

  // not interested in inputs
  private val in: Sink[JsValue, NotUsed] =
    Flow[JsValue].to(Sink.ignore)


  private def out(userId: UserId) =
    Consumer
      .committableSource(consumerSettings(userId), Subscriptions.topics(comps.config.kafkaWsPathMsgTopic))
      .mapAsync(10) { msg =>
        business(msg.record)
        //.map(_=>msg.committableOffset)

      }

  //  .mapAsync(5)(offset => offset.commitScaladsl())
  // .toMat(Sink.ignore)(Keep.both)
  //  .mapMaterializedValue(DrainingControl.apply)
  // .run()

  def business(record:ConsumerRecord[String,String]): Future[JsValue] = {
    Future.successful(PathMsg(record.key, record.value, record.timestamp()))
      .map(Json.toJson[PetWebSocketMsg])
  }
}
