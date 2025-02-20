package controllers

import javax.inject._
import java.util.UUID
import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.Reply
import models.service.ReplyService
import utils.{ SecureAction, CHErrorHandler }

@Singleton
class ReplyController @Inject()(
  val controllerComponents: ControllerComponents,
  replyService: ReplyService, 
  SecureAction: SecureAction
)(implicit ec: ExecutionContext) extends BaseController {

  def index = SecureAction.async {
    replyService.getReplies.map(replies => Ok(Json.obj("replies" -> Json.toJson(replies))))
  }

  def create = SecureAction.async(parse.json) { request =>
    val json = request.body.validate[Reply]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
      reply => {
        replyService.createReply(reply).fold(CHErrorHandler(_), success => Created(Json.obj("reply" -> Json.toJson(reply))))
      }
    )
  }

  def edit(id: UUID) = SecureAction.async(parse.json) { request =>
    val text = (request.body \ "reply").as[String]
    replyService.editReply(id, text).fold(CHErrorHandler(_), success => Ok(Json.obj("success" -> s"$success")))
  }

  def destroy(id: UUID) = SecureAction.async {
    replyService.deleteReply(id).fold(CHErrorHandler(_), success => Ok(Json.obj("success" -> s"$success")))
  }
}