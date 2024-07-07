package models.repo

import javax.inject.{ Singleton, Inject }
import java.util.UUID
import java.time.Instant

import play.api.db.slick.{ DatabaseConfigProvider, HasDatabaseConfigProvider }

import scala.concurrent.{ Future, ExecutionContext }
import scala.util.{ Success, Failure, Try }

import slick.jdbc.JdbcProfile

import models.domain.User

@Singleton
class UserRepo @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends
  HasDatabaseConfigProvider[JdbcProfile] {
    import profile.api._

    class UserTable(tag: Tag) extends Table[User](tag, "USERS") {
      def id = column[UUID]("ID", O.PrimaryKey)
      def email = column[String]("EMAIL", O.Unique, O.Length(255, true))
      def password = column[String]("PASSWORD")
      def createdAt = column[Instant]("CREATED_AT")

      def * = (id, email, password, createdAt).mapTo[User]
    }

    object users extends TableQuery(new UserTable(_)) {
      def create(user: User): Future[Try[Int]] = {
        val action = (this += user).asTry
        db.run(action)
      }
    }
}