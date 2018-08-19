package pme123.petstore.server.control

import akka.NotUsed
import akka.kafka.scaladsl.Producer
import akka.kafka.{ProducerMessage, ProducerSettings}
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import javax.inject.Inject
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import play.api.libs.json.{JsError, JsString, JsSuccess, JsValue}
import pme123.petstore.server.boundary.services.SPAComponents
import pme123.petstore.shared.services.Logging
import pme123.petstore.shared.{PathMsg, PetWebSocketMsg}

import scala.concurrent.{ExecutionContext, Future}

class PathMsgProducer @Inject()(comps: SPAComponents)
                               (implicit val ec: ExecutionContext,
                                mat: Materializer)
  extends Logging {

  /**
    * Creates a Future containing a Flow of JsValue in and out.
    */
  def producerFlow(): Future[Flow[JsValue, JsValue, NotUsed]] = {
    Future.successful(
      Flow.fromSinkAndSource(in, out))

  }

  private val kafkaConfig = comps.config.kafkaWsPathMsgProducer.underlying
  private val producerSettings =
    ProducerSettings(kafkaConfig, new StringSerializer, new StringSerializer)
      .withBootstrapServers(comps.config.kafkaWsBootstrapServers)

  // Send events to Kafka Topic
  private val in: Sink[JsValue, NotUsed] =
    Flow[JsValue].map { json =>
      json.validate[PetWebSocketMsg] match {
        case JsSuccess(pathMsg: PathMsg, _) =>
          Some(pathMsg)
        //   jobActor ! runAdapter
        case JsSuccess(other, _) =>
          warn(s"Unexpected message received: $other")
          None
        case JsError(errors) =>
          error("Other than PetWebSocketMsg: " + errors.toString())
          None
      }
    }.filter(_.nonEmpty)
      .map(_.get)
      .map { msg =>
        ProducerMessage.Message(
          new ProducerRecord(comps.config.kafkaWsPathMsgTopic, msg.username, s"${msg.msg},${msg.time}"),
          "passThrough"
        )
      }.via(Producer.flexiFlow(producerSettings))
      .to(Sink.ignore)


  // Send a single 'Hello!' message and then leave the socket open
  private val out: Source[JsValue, NotUsed] = Source.single(JsString("Hello!")).concat(Source.maybe)

}
