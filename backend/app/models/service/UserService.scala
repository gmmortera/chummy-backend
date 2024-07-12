package models.service

import javax.inject.{ Singleton, Inject }

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.{ EitherT, OptionT }
import cats.syntax.all._

import models.domain.{ User, LoginData }
import models.repo.UserRepo
import utils.{ CHError, ServiceError }

@Singleton
class UserService @Inject()(userRepo: UserRepo)(implicit ec: ExecutionContext) extends CHError {
  
  def createUser(user: User): EitherT[Future, ServiceError, String] = EitherT {
    val query = userRepo.users.create(user)
    query.map { result => result.fold(
        error => Left(USER_ERROR_REGISTER),
        success => Right("User added successfully")
      )
    }
  }

  def validateUser(user: LoginData): EitherT[Future, ServiceError, User] = {
    OptionT(userRepo.users.findByUsernameAndPassword(user)).toRight(USER_ERROR_LOGIN)
  }
}