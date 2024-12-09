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
  signatire: Option[String]
)

object Asset {
  implicit val assetFormat: Format[Asset] = Json.format[Asset]
}