package models.domain

import java.util.UUID
import java.time.Instant

import play.api.libs.json._
import play.api.libs.functional.syntax._


case class Comment(
  id: UUID,
  idUser: UUID,
  idPost: UUID,
  text: String,
  createdAt: Instant
) {
  def withId(id: UUID): Comment = new Comment(
    id,
    idUser,
    idPost,
    text,
    createdAt
  )
}

object Comment {
  implicit val commentReads: Reads[Comment] = (
    Reads.pure(UUID.randomUUID) and
    (JsPath \ "idUser").read[UUID] and
    (JsPath \ "idPost").read[UUID] and
    (JsPath \ "text").read[String] and
    Reads.pure(Instant.now)
  )(Comment.apply _)
  implicit val commentWrites: Writes[Comment] = Json.writes[Comment]
}