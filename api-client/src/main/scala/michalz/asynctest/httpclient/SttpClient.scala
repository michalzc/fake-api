package michalz.asynctest.httpclient

import com.softwaremill.sttp._
import com.softwaremill.sttp.asynchttpclient.future.AsyncHttpClientFutureBackend
import com.typesafe.scalalogging.LazyLogging
import michalz.asynctest.httpclient.HttpClient.ServiceResponse

import scala.concurrent.{ExecutionContext, Future}

object SttpClient extends HttpClient with LazyLogging {
  implicit val sttpBackend = AsyncHttpClientFutureBackend()

  override def perform(url: String)(implicit ec: ExecutionContext): Future[ServiceResponse[String]] = {
    sttp.get(uri"$url").send().map { response =>
      if(response.code == 200)
        response.body
      else
        Left(s"Request failed, Code: ${response.code}, Body: ${response.body}")
    }
  }

  def shutdown() = sttpBackend.close()
}
