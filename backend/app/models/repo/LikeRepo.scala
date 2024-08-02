package models.repo

import javax.inject.{ Singleton, Inject }
import java.util.UUID
import java.time.Instant

import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.Try

import slick.jdbc.JdbcProfile

import models.domain.Like
import models.repo.{ UserRepo, PostRepo }

@Singleton
class LikeRepo @Inject()(
  protected val dbConfigProvider: DatabaseConfigProvider,
  val userRepo: UserRepo,
  val postRepo: PostRepo
)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  class LikeTable(tag: Tag) extends Table[Like](tag, "LIKES") {
    def idUser = column[UUID]("ID_USER")
    def idPost = column[UUID]("ID_POST")
    def isLiked = column[Boolean]("IS_LIKED")
    def createdAt = column[Instant]("CREATED_AT")

    def pkLike = primaryKey("PK_LIKE", (idUser, idPost))
    def likeUser = foreignKey("LIKE_USER", idUser, userRepo.users.table)(_.id, onDelete=ForeignKeyAction.Cascade)
    def likePost = foreignKey("LIKE_POST", idPost, postRepo.posts.table)(_.id, onDelete=ForeignKeyAction.Cascade)

    def * = (idUser, idPost, isLiked, createdAt).mapTo[Like]
  }

  object likes extends TableQuery(new LikeTable(_)) {
    def get: Future[Seq[Like]] = db.run(this.result)
    def create(like: Like): Future[Try[Int]] = {
      val action = (this += like).asTry
      db.run(action)
    }
    def edit(like: Like): Future[Try[Int]] = {
      val action = this
        .filter(l => (l.idUser === like.idUser && l.idPost === like.idPost))
        .update(like).asTry
      db.run(action)
    }
  }
}
