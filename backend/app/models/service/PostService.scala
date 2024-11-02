package models.service

import javax.inject.{ Singleton, Inject }
import java.util.UUID

import play.api.http.Status

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.EitherT
import cats.syntax.all._

import models.domain.Post
import models.repo.PostRepo
import models.service.WebsocketService
import utils.CHError
import utils.result.CHResult

@Singleton
class PostService @Inject()(
  postRepo: PostRepo,
  websocketService: WebsocketService
)(implicit ec: ExecutionContext) {

  def getPosts: Future[Seq[Post]] = postRepo.posts.get

  def createPost(post: Post): CHResult[String] = EitherT {
    val query = postRepo.posts.post(post)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "post.error.create")),
      _ => {
        websocketService.broadcastNewPost(post)
        Right("Post added successfully")
      }
    )}
  }

  def deletePost(id: UUID): CHResult[String] = EitherT {
    val query = postRepo.posts.destroy(id)
    query.map { _.fold(
      _ => Left(CHError(Status.NOT_FOUND, "post.error.delete")),
      _ => Right("Post deleted successfully")
    )}
  }
}