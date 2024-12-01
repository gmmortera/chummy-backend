package models.repo

import javax.inject.{ Singleton, Inject }
import java.util.UUID
import java.time.Instant

import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.Try

import slick.jdbc.JdbcProfile

import models.domain.Profile
import models.repo.UserRepo

@Singleton
class ProfileRepo @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider,
  val userRepo: UserRepo
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  class ProfileTable(tag: Tag) extends Table[Profile](tag, "PROFILES") {
    def idUser = column[UUID]("ID_USER", O.PrimaryKey)
    def username = column[Option[String]]("USERNAME", O.Length(255, true))
    def image = column[Option[String]]("IMAGE")
    def birthday = column[Option[Instant]]("BIRTHDAY")

    def userProfile = foreignKey("USER_PROFILE", idUser, userRepo.users.table)(_.id, onDelete=ForeignKeyAction.Cascade)

    def * = (idUser, username, image, birthday).mapTo[Profile]
  }

  object profiles extends TableQuery(new ProfileTable(_)) {
    def get: Future[Seq[Profile]] = db.run(this.result)
    def create(profile: Profile): Future[Try[Int]] = db.run((this += profile).asTry)
    def edit(profile: Profile): Future[Try[Int]] = {
      val action = this
        .filter(_.idUser === profile.idUser)
        .map(p => (p.idUser, p.username, p.image, p.birthday).mapTo[Profile])
        .update(profile).asTry
      db.run(action)
    }
  }
}