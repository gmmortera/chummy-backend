package models.domain

import java.util.UUID
import java.time.Instant

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Notification(
  id: UUID,
  idUser: UUID,
  idPost: UUID,
  action: ActionType.ActionType,
  createdAt: Instant,
  seenAt: Option[Instant]
) {
  def withId(id: UUID): Notification = new Notification(
    id,
    idUser,
    idPost,
    action,
    createdAt,
    Some(Instant.now)
  )
}

object Notification {
  implicit val notifReads: Reads[Notification] = (
    Reads.pure(UUID.randomUUID) and
    (JsPath \ "idUser").read[UUID] and
    (JsPath \ "idPost").read[UUID] and
    (JsPath \ "action").read[String] and
    Reads.pure(Instant.now) and
    (JsPath \ "seenAt").readNullable[Instant]
  )((id, idUser, idPost, action, createdAt, seenAt) => 
      Notification(id, idUser, idPost, ActionType.withName(action), createdAt, seenAt)
    )
  implicit val notifWrites: Writes[Notification] = Json.writes[Notification]
}

case object ActionType extends Enumeration {
  type ActionType = Value
  val REPLY = Value("REPLY")
  val COMMENT = Value("COMMENT")
  val LIKE = Value("LIKE")
}
