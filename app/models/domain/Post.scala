package models.domain

import play.api.libs.json._
import play.api.libs.functional.syntax._

import java.time.Instant
import java.util.UUID

case class Post(
  id: UUID,
  idUser: UUID,
  image: Option[String],
  content: String,
  createdAt: Instant
)

object Post {
  implicit val postReads: Reads[Post] = (
    Reads.pure(UUID.randomUUID) and
    (JsPath \ "idUser").read[UUID] and
    (JsPath \ "image").readNullable[String] and
    (JsPath \ "content").read[String] and
    Reads.pure(Instant.now)
  )(Post.apply _)

  implicit val postWrites: Writes[Post] = Json.writes[Post]
}
