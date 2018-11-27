package michalz.asynctest.httpclient

import io.circe.Decoder
import michalz.asynctest.httpclient.HttpClient.ServiceResponse

import scala.concurrent.{ExecutionContext, Future}

trait HttpClient {

  def makeRequests[A](baseUrl: String, numberOfItems: Int)
    (implicit ec: ExecutionContext, contentDecoder: Decoder[A]): Future[List[ServiceResponse[A]]] = Future.sequence(
    (1 to numberOfItems)
      .toList
      .map(idx => f"$baseUrl/$idx%08d")
      .map(makeRequest[A])
  )

  def makeRequest[A](url: String)
    (implicit ec: ExecutionContext, decoder: Decoder[A]): Future[ServiceResponse[A]]
}

object HttpClient {
  type ServiceResponse[A] = Either[String, A]
}
