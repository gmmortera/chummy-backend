package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.Like
import models.service.LikeService
import utils.{ CHErrorHandler, SecureAction }

@Singleton
class LikeController @Inject()(
  val controllerComponents: ControllerComponents,
  likeService: LikeService,
  SecureAction: SecureAction
)(implicit ec: ExecutionContext) extends BaseController {
  
  def index = SecureAction.async {
    likeService.getLikes.map(likes => Ok(Json.obj("likes" -> Json.toJson(likes))))
  }

  def create = SecureAction.async(parse.json) { implicit request =>
    val json = request.body.validate[Like]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("errors" -> JsError.toJson(error)))),
      like => {
        likeService.createLike(like).fold(CHErrorHandler(_), success => Created(Json.obj("success" -> s"$success")))
      }
    )
  }

  def edit = SecureAction.async(parse.json) { implicit request =>
    val json = request.body.validate[Like]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("errors" -> JsError.toJson(error)))),
      like => {
        likeService.editLike(like).fold(CHErrorHandler(_), success => Ok(Json.obj("success" -> s"$success")))
      }
    )
  }
}