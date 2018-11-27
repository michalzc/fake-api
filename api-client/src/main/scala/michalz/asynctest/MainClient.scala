package michalz.asynctest

import java.util.concurrent.Executors

import com.typesafe.scalalogging.LazyLogging
import michalz.asynctest.httpclient.ScalaJHttpClient
import michalz.asynctest.model.Item

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext}

object MainClient extends App with LazyLogging {
  logger.info("Starting main client")

  val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(3))

  val response = ScalaJHttpClient.makeRequests[Item]("http://localhost:5000/items", 10)(ec, Item.decoder)
  logger.info("Responses sent")
  val items = Await.result(response, Duration.Inf)
  items.foreach {
    case Left(msg) =>
      logger.warn(s"Request failed: $msg")

    case Right(item) =>
      logger.info(s"Item received: $item")
  }


}
