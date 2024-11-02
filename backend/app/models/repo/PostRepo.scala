package models.repo

import javax.inject.{ Singleton, Inject }
import java.util.UUID
import java.time.Instant

import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.Try

import slick.jdbc.JdbcProfile

import models.domain.Post
import models.repo.UserRepo

@Singleton
class PostRepo @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider,
  val userRepo: UserRepo
  )(implicit ec: ExecutionContext) extends 
  HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._

    class PostTable(tag: Tag) extends Table[Post](tag, "POSTS") {
      def id = column[UUID]("ID", O.PrimaryKey)
      def idUser = column[UUID]("ID_USER")
      def image = column[Option[String]]("IMAGE")
      def content = column[String]("CONTENT")
      def createdAt = column[Instant]("CREATED_AT")

      def userPost = foreignKey("USER_POST", idUser, userRepo.users.table)(_.id, onDelete=ForeignKeyAction.Cascade)

      def * = (id, idUser, image, content, createdAt).mapTo[Post]
    }

    object posts extends TableQuery(new PostTable(_)) {
      def table: TableQuery[PostTable] = this
      def get: Future[Seq[Post]] = db.run(this.result)
      def post(post: Post): Future[Try[Int]] = {
        val action = (this += post).asTry
        db.run(action)
      }
      def destroy(id: UUID): Future[Try[Int]] = {
        val action = (this.filter(_.id === id).delete).asTry
        db.run(action)
      }
    }
}