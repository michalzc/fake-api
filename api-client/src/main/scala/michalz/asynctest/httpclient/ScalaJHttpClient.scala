package michalz.asynctest.httpclient

import com.typesafe.scalalogging.LazyLogging
import michalz.asynctest.httpclient.HttpClient.ServiceResponse
import scalaj.http.{Http, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

object ScalaJHttpClient extends HttpClient with LazyLogging {

  override def perform(url: String)
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
