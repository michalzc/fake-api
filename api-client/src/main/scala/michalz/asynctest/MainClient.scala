package michalz.asynctest

import java.util.concurrent.Executors

import com.typesafe.scalalogging.LazyLogging
import michalz.asynctest.httpclient.{ApacheAsyncClient, ScalaJHttpClient, SttpClient}
import michalz.asynctest.model.Item

import scala.collection.JavaConverters._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object MainClient extends App with LazyLogging {
  logger.info("Starting main client")

  private val service = Executors.newFixedThreadPool(1)
  val ec: ExecutionContext = ExecutionContext.fromExecutor(service)

  val response = ApacheAsyncClient.makeRequests[Item]("http://localhost:5000/items", 30)(ec, Item.decoder)
  logger.info("Responses sent")
  val items = Await.result(response, Duration.Inf)
  items.foreach {
    case Left(msg) =>
      logger.warn(s"Request failed: $msg")

    case Right(item) =>
      logger.info(s"Item received: $item")
  }

  ApacheAsyncClient.shutdown()
//  service.shutdownNow().asScala.foreach { r =>
//    logger.info(s"In progress: $r")
//  }
}

