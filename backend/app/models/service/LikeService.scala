package models.service

import javax.inject.{ Singleton, Inject }

import play.api.http.Status

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.EitherT
import cats.syntax.all._

import models.domain.Like
import models.repo.LikeRepo
import utils.result.CHResult
import utils.CHError

@Singleton
class LikeService @Inject()(likeRepo: LikeRepo)(implicit ec: ExecutionContext) {
  def getLikes: Future[Seq[Like]] = likeRepo.likes.get
  def createLike(like: Like): CHResult[String] = EitherT {
    val query = likeRepo.likes.create(like)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "like.error.create")),
      _ => Right("Like added successfully")
    )}
  }
  def editLike(like: Like): CHResult[String] = EitherT {
    val query = likeRepo.likes.edit(like)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "like.error.edit")),
      _ => Right("Like edited successfully")
    )}
  }
}