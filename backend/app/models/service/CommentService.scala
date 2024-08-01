package models.service

import javax.inject.{ Singleton, Inject }
import java.util.UUID

import play.api.http.Status

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.EitherT
import cats.syntax.all._

import models.domain.Comment
import models.repo.CommentRepo
import utils.result.CHResult
import utils.CHError

@Singleton
class CommentService @Inject()(commentRepo: CommentRepo)(implicit ec: ExecutionContext) {
  
  def getComments: Future[Seq[Comment]] = commentRepo.comments.get
  def createComment(comment: Comment): CHResult[String] = EitherT {
    val query = commentRepo.comments.create(comment)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "comment.error.create")),
      _ => Right("Comment added successfully")
    )}
  }
  def editComment(comment: Comment): CHResult[String] = EitherT {
    val query = commentRepo.comments.edit(comment)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "comment.error.edit")),
      _ => Right("Comment edited successfully")
    )}
  }
  def deleteComment(id: UUID): CHResult[String] = EitherT {
    val query = commentRepo.comments.destroy(id)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "comment.error.delete")),
      _ => Right("Comment deleted successfully")
    )}
  }
}