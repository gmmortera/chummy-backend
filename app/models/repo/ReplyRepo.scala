package models.repo

import javax.inject.{ Singleton, Inject }
import java.util.UUID
import java.time.Instant

import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.Try

import slick.jdbc.JdbcProfile

import models.domain.Reply
import models.repo.{ UserRepo, CommentRepo }

@Singleton
class ReplyRepo @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider,
  val userRepo: UserRepo,
  val commentRepo: CommentRepo
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  class ReplyTable(tag: Tag) extends Table[Reply](tag, "REPLIES") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def idUser = column[UUID]("ID_USER")
    def idSender = column[UUID]("ID_SENDER")
    def idComment = column[UUID]("ID_COMMENT")
    def text = column[String]("TEXT")
    def createdAt = column[Instant]("CREATED_AT")
    def updatedAt = column[Option[Instant]]("UPDATED_AT")

    def replyUser = foreignKey("REPLY_USER", idUser, userRepo.users.table)(_.id, onDelete=ForeignKeyAction.Cascade)

    def * = (id, idUser, idSender, idComment, text, createdAt, updatedAt).mapTo[Reply]
  }

  object replies extends TableQuery(new ReplyTable(_)) {
    def get: Future[Seq[Reply]] = db.run(this.result)
    def create(reply: Reply): Future[Try[Int]] = {
      val action = (this += reply).asTry
      db.run(action)
    }
    def edit(id: UUID, text: String): Future[Try[Int]] = {
      val action = this
        .filter(_.id === id)
        .map(r => (r.text, r.updatedAt))
        .update((text, Some(Instant.now))).asTry
      db.run(action)
    }
    def destroy(id: UUID): Future[Try[Int]] = {
      val action = (this.filter(_.id === id).delete).asTry
      db.run(action)
    }
  }
}