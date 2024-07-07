package models.domain

import java.util.UUID
import java.time.Instant
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class User(
  id: UUID,
  email: String,
  password: String,
  createdAt: Instant
)

object User {
  implicit val userReads: Reads[User] = (
    Reads.pure(UUID.randomUUID) and
    (JsPath \ "email").read[String] and
    (JsPath \ "password").read[String] and
    Reads.pure(Instant.now)
  )(User.apply _)
}