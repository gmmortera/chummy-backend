package models.domain

import play.api.libs.json._
import play.api.libs.functional.syntax._

import java.util.UUID
import java.time.Instant

case class Profile(
  idUser: UUID,
  username: Option[String],
  image: Option[String],
  birthday: Option[Instant]
)

object Profile {
  implicit val profileFormat: Format[Profile] = Json.format[Profile]
}