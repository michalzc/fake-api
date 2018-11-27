package michalz.asynctest.httpclient

import com.typesafe.scalalogging.LazyLogging
import io.circe.Decoder
import io.circe.parser.parse
import michalz.asynctest.httpclient.HttpClient.ServiceResponse
import scalaj.http.{Http, HttpResponse}
import cats.syntax.either._

import scala.concurrent.{ExecutionContext, Future}

object ScalaJHttpClient extends HttpClient with LazyLogging {
  override def makeRequest[A](url: String)(implicit ec: ExecutionContext, decoder: Decoder[A]): Future[ServiceResponse[A]] = {
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

  def perform[A](url: String)
    (implicit ec: ExecutionContext): Future[ServiceResponse[String]] = Future {
    logger.info(s"Sending request to $url")
    Http(url).asString match {
      case HttpResponse(body, code, _) if code == 200 =>
        Right(body)

      case HttpResponse(body, code, headers) =>
        Left(s"Unknown response (code=$code, body=$body)")
    }
  }
}
