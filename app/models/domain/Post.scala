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
  createdAt: Instant,
  signature: Option[String]
) {
  def addImage(image: String, signature: String): Post = {
    Post(
      id,
      idUser,
      Some(image),
      content,
      createdAt,
      Some(signature)
    )
  }
}

object Post {
  implicit val postReads: Reads[Post] = (
    Reads.pure(UUID.randomUUID) and
    (JsPath \ "idUser").read[UUID] and
    (JsPath \ "image").readNullable[String] and
    (JsPath \ "content").read[String] and
    Reads.pure(Instant.now) and
    (JsPath \ "signature").readNullable[String]
  )(Post.apply _)

  implicit val postWrites: Writes[Post] = Json.writes[Post]
}

case class Asset(
  url: Option[String],
  signature: Option[String]
)

object Asset {
  implicit val assetFormat: Format[Asset] = Json.format[Asset]
}

case class CursorRequest(
  cursor: Option[Instant],
  limit: Int
)

object CursorRequest {
  implicit val cursorRequestReads: Reads[CursorRequest] = Json.reads[CursorRequest]
}

case class CursorResponse(
  posts: Seq[Post] ,
  nextCursor: Option[Instant],
  hasMore: Boolean
)

object CursorResponse {
  implicit val cursorResponseWrites: Writes[CursorResponse] = Json.writes[CursorResponse]
}
