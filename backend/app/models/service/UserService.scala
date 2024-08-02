package models.service

import javax.inject.{ Singleton, Inject }

import play.api.http.Status

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.{ EitherT, OptionT }
import cats.syntax.all._

import models.domain.{ User, LoginData }
import models.repo.UserRepo
import utils.CHError
import utils.result.CHResult

@Singleton
class UserService @Inject()(userRepo: UserRepo)(implicit ec: ExecutionContext) {
  def getUsers: Future[Seq[User]] = userRepo.users.get
  def createUser(user: User): CHResult[String] = EitherT {
    val query = userRepo.users.create(user)
    query.map { _.fold(
      _ => Left(CHError(Status.FORBIDDEN, "user.error.register")),
      _ => Right("User added successfully")
    )}
  }

  def validateUser(user: LoginData): EitherT[Future, CHError, User] = {
    OptionT(userRepo.users.findByUsernameAndPassword(user)).toRight(CHError(Status.UNAUTHORIZED, "user.error.login"))
  }
}