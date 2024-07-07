package models.service

import javax.inject.{ Singleton, Inject }

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.EitherT
import cats.syntax.all._

import models.domain.User
import models.repo.UserRepo

@Singleton
class UserService @Inject()(userRepo: UserRepo)(implicit ec: ExecutionContext) {
  
  def createUser(user: User): EitherT[Future, String, String] = EitherT {
    val query = userRepo.users.create(user)
    query.map { result => result.fold(
        error => Left("User already exists"),
        success => Right("User added successfully")
      )
    }
  }
}