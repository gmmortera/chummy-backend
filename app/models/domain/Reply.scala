package models.domain

import java.util.UUID
import java.time.Instant

import play.api.libs.json._
import play.api.libs.functional.syntax._


case class Reply(
  id: UUID,
  idUser: UUID,
  idSender: UUID,
  idComment: UUID,
  text: String,
  createdAt: Instant,
  updatedAt: Option[Instant]
)

object Reply {
  implicit val replyReads: Reads[Reply] = (
    Reads.pure(UUID.randomUUID) and
    (JsPath \ "idUser").read[UUID] and
    (JsPath \ "idSender").read[UUID] and
    (JsPath \ "idComment").read[UUID] and
    (JsPath \ "text").read[String] and
    Reads.pure(Instant.now) and
    (JsPath \ "updatedAt").readNullable[Instant]
  )(Reply.apply _)
  implicit val replyWrites: Writes[Reply] = Json.writes[Reply]
}
