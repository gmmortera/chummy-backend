package models.service

import javax.inject.{ Singleton, Inject }
import java.util.UUID

import play.api.http.Status

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.EitherT
import cats.syntax.all._

import models.domain.{ Like, LikeStatus }
import models.repo.LikeRepo
import models.service.WebsocketService
import utils.result.CHResult
import utils.CHError

@Singleton
class LikeService @Inject()(
  likeRepo: LikeRepo,
  websocketService: WebsocketService
)(implicit ec: ExecutionContext) {
  def getLikes: Future[Seq[Like]] = likeRepo.likes.get
  def createLike(like: Like): CHResult[String] = EitherT {
    val query = likeRepo.likes.create(like)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "like.error.create")),
      _ => {
        websocketService.broadcastNewLike(like) 
        Right("Like added successfully")
      }
    )}
  }
  def editLike(idUser: UUID, status: LikeStatus): CHResult[String] = EitherT {
    val query = likeRepo.likes.edit(idUser, status)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "like.error.edit")),
      _ => Right("Like edited successfully")
    )}
  }
}