package michalz.asynctest.model

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

case class Item(
  itemId: String,
  description: String
)

object Item {
  implicit val encoder: Encoder[Item] = deriveEncoder[Item]
  implicit val decoder: Decoder[Item] = deriveDecoder[Item]
}