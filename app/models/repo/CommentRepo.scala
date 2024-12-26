package models.repo

import javax.inject.{ Singleton, Inject }
import java.util.UUID
import java.time.Instant

import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.Try

import slick.jdbc.JdbcProfile

import models.domain.Comment
import models.repo.{ UserRepo, PostRepo }

@Singleton
class CommentRepo @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider,
  val userRepo: UserRepo,
  val postRepo: PostRepo
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  class CommentTable(tag: Tag) extends Table[Comment](tag, "COMMENTS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def idUser = column[UUID]("ID_USER")
    def idPost = column[UUID]("ID_POST")
    def text = column[String]("TEXT")
    def createdAt = column[Instant]("CREATED_AT")
    def updatedAt = column[Option[Instant]]("UPDATED_AT")

    def commentUser = foreignKey("COMMENT_USER", idUser, userRepo.users.table)(_.id, onDelete=ForeignKeyAction.Cascade)
    def commentPost = foreignKey("COMMENT_POST", idPost, postRepo.posts.table)(_.id, onDelete=ForeignKeyAction.Cascade)

    def * = (id, idUser, idPost, text, createdAt, updatedAt).mapTo[Comment]
  }

  object comments extends TableQuery(new CommentTable(_)) {
    def table: TableQuery[CommentTable] = this
    def get: Future[Seq[Comment]] = {
      val action = this.sortBy(_.createdAt.desc).result
      db.run(action)
    }
    def create(comment: Comment): Future[Try[Int]] = {
      val action = (this += comment).asTry
      db.run(action)
    }
    def edit(id: UUID, text: String): Future[Try[Int]] = {
      val action = this
        .filter(_.id === id)
        .map(c => (c.text, c.updatedAt))
        .update((text, Some(Instant.now))).asTry
      db.run(action)
    }
    def destroy(id: UUID): Future[Try[Int]] = {
      val action = (this.filter(_.id === id).delete).asTry
      db.run(action)
    }
  }
}