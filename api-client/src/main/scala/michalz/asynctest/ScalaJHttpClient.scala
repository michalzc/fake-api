package michalz.asynctest

import com.typesafe.scalalogging.LazyLogging
import michalz.asynctest.model.Item

import scala.concurrent.Future

object ScalaJHttpClient extends LazyLogging {

  def makeRequests(baseUrl: String, numberOfItems: Int): Future[List[Item]] = ???

}
