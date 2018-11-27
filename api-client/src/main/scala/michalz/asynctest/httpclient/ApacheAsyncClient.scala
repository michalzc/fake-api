package michalz.asynctest.httpclient
import com.typesafe.scalalogging.LazyLogging
import michalz.asynctest.httpclient.HttpClient.ServiceResponse
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.concurrent.FutureCallback
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.io.Source
import scala.util.Try

object ApacheAsyncClient extends HttpClient with LazyLogging {


  val client = HttpAsyncClientBuilder
    .create()
//    .setMaxConnTotal(10)
    .setMaxConnPerRoute(10)
    .build()
  client.start()

  override def perform(url: String)(implicit ec: ExecutionContext): Future[ServiceResponse[String]] = {

    val promise: Promise[ServiceResponse[String]] = Promise()

    val request = new HttpGet(url)
    client.execute(request, new FutureCallback[HttpResponse] {
      logger.info(s"Requesting ${url}")
      override def completed(result: HttpResponse): Unit = {
        val code = result.getStatusLine.getStatusCode
        if (code == 200) {
          val reads = Try {
            Source.fromInputStream(result.getEntity.getContent).mkString
          }
          promise.complete(reads.map(Right(_)))
        } else {
          val reads = Try {
            Source.fromInputStream(result.getEntity.getContent).mkString
          }
          promise.complete(reads.map(result => Left(s"Unknown response: code ${code}, response: $result")))
        }
      }

      override def failed(ex: Exception): Unit = promise.failure(ex)

      override def cancelled(): Unit = promise.failure(new IllegalStateException("Canceled"))
    })

    promise.future
  }


  def shutdown() = {
    client.close()
  }
}
