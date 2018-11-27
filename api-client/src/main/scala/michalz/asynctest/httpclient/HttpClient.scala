package michalz.asynctest.httpclient

import cats.syntax.either._
import com.typesafe.scalalogging.LazyLogging
import io.circe.Decoder
import io.circe.parser.parse
import michalz.asynctest.httpclient.HttpClient.ServiceResponse

import scala.concurrent.{ExecutionContext, Future}

trait HttpClient extends LazyLogging {

  def makeRequests[A](baseUrl: String, numberOfItems: Int)
    (implicit ec: ExecutionContext, contentDecoder: Decoder[A]): Future[List[ServiceResponse[A]]] = Future.sequence(
    (1 to numberOfItems)
      .toList
      .map(idx => f"$baseUrl/$idx%08d")
      .map(makeRequest[A])
  )

  def makeRequest[A](url: String)(implicit ec: ExecutionContext, decoder: Decoder[A]): Future[ServiceResponse[A]] = {
    perform(url).map { response =>
      for {
        responseString <- response
        responseJson <- parse(responseString)
          .leftMap{f => logger.warn(s"Parsing failed: ${f.getMessage()}", f.getCause); f.getMessage() }
        item <- responseJson.as[A]
          .leftMap { f => logger.warn(s"Decoding failed: ${f.getMessage()}", f.getCause); f.getMessage() }
      } yield item
    }
  }

  def perform(url: String)
    (implicit ec: ExecutionContext): Future[ServiceResponse[String]]
}

object HttpClient {
  type ServiceResponse[A] = Either[String, A]
}
