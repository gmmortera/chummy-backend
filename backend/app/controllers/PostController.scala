package controllers

import javax.inject._
import java.util.UUID

import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.Post
import models.service.PostService
import utils.{ SecureAction, CHErrorHandler }

@Singleton
class PostController @Inject()(
  val controllerComponents: ControllerComponents,
  postService: PostService,
  SecureAction: SecureAction
)(implicit ec: ExecutionContext) extends  BaseController {

  def index = SecureAction.async {
    postService.getPosts.map(posts => Ok(Json.obj("posts" -> Json.toJson(posts))))
  }

  def create = SecureAction.async(parse.json) { implicit request =>
    val json = request.body.validate[Post]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
      post => {
        postService.createPost(post).fold(CHErrorHandler(_), success => Created(Json.obj("post" -> Json.toJson(post))))
      }
    )
  }

  def destroy(id: UUID) = SecureAction.async { implicit request =>
    postService.deletePost(id).fold(CHErrorHandler(_), success => NotFound(Json.obj("success" -> s"$success")))  
  }
}