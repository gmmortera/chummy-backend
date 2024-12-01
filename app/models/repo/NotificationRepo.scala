package models.repo

import javax.inject.{ Singleton, Inject }
import java.util.UUID
import java.time.Instant

import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.Try

import slick.jdbc.JdbcProfile

import models.domain.ActionType
import models.domain.Notification
import models.repo.{ UserRepo, PostRepo }

@Singleton
class NotificationRepo @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider,
  val userRepo: UserRepo,
  val postRepo: PostRepo
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._
  import models.domain.ActionType.ActionType

  implicit val actionMapper: BaseColumnType[ActionType] =
    MappedColumnType.base[ActionType, String](
      _.toString,
      ActionType.withName(_)
    )

  class NotificationTable(tag: Tag) extends Table[Notification](tag, "NOTIFICATIONS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def idUser = column[UUID]("ID_USER")
    def idPost = column[UUID]("ID_POST")
    def action = column[ActionType]("ACTION", O.Length(10, true))
    def createdAt = column[Instant]("CREATED_AT")
    def seenAt = column[Option[Instant]]("SEEN_AT")

    def notifUser = foreignKey("NOTIF_USER", idUser, userRepo.users.table)(_.id, 
      onDelete=ForeignKeyAction.Cascade)
    def notifPost = foreignKey("NOTIF_POST", idPost, postRepo.posts.table)(_.id, 
      onDelete=ForeignKeyAction.Cascade)

    def * = (id, idUser, idPost, action, createdAt, seenAt).mapTo[Notification]
  }

  object notifications extends TableQuery(new NotificationTable(_)) {
    def get: Future[Seq[Notification]] = db.run(this.result)
    def create(notification: Notification): Future[Try[Int]] = {
      val action = (this += notification).asTry
      db.run(action)
    }
    def edit(id: UUID): Future[Try[Int]] = {
      val action = this
        .filter(_.id === id)
        .map(_.seenAt)
        .update(Some(Instant.now)).asTry
      db.run(action)
    }
  }
}