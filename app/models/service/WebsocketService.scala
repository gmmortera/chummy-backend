package models.service

import javax.inject.{ Singleton, Inject, Named }

import play.api.mvc._
import play.api.libs.streams.ActorFlow
import play.api.libs.json._

import scala.concurrent.{ Future, ExecutionContext }

import org.apache.pekko.actor.{ ActorSystem, Props, ActorRef }
import org.apache.pekko.stream.Materializer
import org.apache.pekko.stream.scaladsl._

import models.domain._
import actors._

@Singleton
class WebsocketService @Inject()(@Named("general-manager") manager: ActorRef)(
  implicit system: ActorSystem,
  mat: Materializer,
  executionContext: ExecutionContext
) {

  def broadcastNewPost(post: Post): Unit = {
    val action: UserAction = UserAction("new-post", Json.toJson(post))
    manager ! GeneralManager.NewPost(post.idUser, action)
  }

  def broadcastNewComment(comment: Comment): Unit = {
    val action: UserAction = UserAction("new-comment", Json.toJson(comment))
    manager ! GeneralManager.NewComment(comment.idUser, action)
  }

  def broadcastNewLike(like: Like): Unit = {
    val action: UserAction = UserAction("new-like", Json.toJson(like))
    manager ! GeneralManager.NewLike(like.idUser, action)
  }
}