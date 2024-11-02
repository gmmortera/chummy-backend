package models.service

import javax.inject.{ Singleton, Inject }
import java.util.UUID

import play.api.http.Status

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.EitherT
import cats.syntax.all._

import models.domain.Reply
import models.repo.ReplyRepo
import utils.result.CHResult
import utils.CHError

@Singleton
class ReplyService @Inject()(replyRepo: ReplyRepo)(implicit ec: ExecutionContext) {
  
  def getReplies: Future[Seq[Reply]] = replyRepo.replies.get
  def createReply(reply: Reply): CHResult[String] = EitherT {
    val query = replyRepo.replies.create(reply)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "reply.error.create")),
      _ => Right("Reply added successfully")
    )}
  }
  def editReply(id: UUID, text: String): CHResult[String] = EitherT {
    val query = replyRepo.replies.edit(id, text)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "reply.error.edit")),
      _ => Right("Reply edited successfully")
    )}
  }
  def deleteReply(id: UUID): CHResult[String] = EitherT {
    val query = replyRepo.replies.destroy(id)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "reply.error.delete")),
      _ => Right("Reply deleted successfully")
    )}
  }
}