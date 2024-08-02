package models.domain

import play.api.libs.json._
import play.api.libs.functional.syntax._

import java.time.Instant
import java.util.UUID

case class Like(
  idUser: UUID,
  idPost: UUID,
  isLiked: Boolean,
  createdAt: Instant
)

object Like {
  implicit val likeReads: Reads[Like] = (
    (JsPath \ "idUser").read[UUID] and
    (JsPath \ "idPost").read[UUID] and
    (JsPath \ "isLiked").read[Boolean] and
    Reads.pure(Instant.now)
  )(Like.apply _)

  implicit val likeWrites: Writes[Like] = Json.writes[Like]
}