package controllers

import javax.inject._
import java.util.UUID
import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.Comment
import models.service.CommentService
import utils.{ SecureAction, CHErrorHandler }

@Singleton
class CommentController @Inject()(
  val controllerComponents: ControllerComponents,
  commentService: CommentService, 
  SecureAction: SecureAction
)(implicit ec: ExecutionContext) extends BaseController {

  def index = SecureAction.async {
    commentService.getComments.map(comments => Ok(Json.toJson(comments)))
  }

  def create = SecureAction.async(parse.json) { request =>
    val json = request.body.validate[Comment]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
      comment => {
        commentService.createComment(comment).fold(CHErrorHandler(_), success => Created(Json.toJson(comment)))
      }
    )
  }

  def edit(id: UUID) = SecureAction.async(parse.json) { request =>
    val text = (request.body \ "comment").as[String]
    commentService.editComment(id, text).fold(CHErrorHandler(_), success => Ok(Json.obj("success" -> s"$success")))
  }

  def destroy(id: UUID) = SecureAction.async {
    commentService.deleteComment(id).fold(CHErrorHandler(_), success => Ok(Json.obj("success" -> s"$success")))
  }
}