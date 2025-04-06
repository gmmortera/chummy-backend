package controllers

import javax.inject._
import java.util.UUID
import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.Notification
import models.service.NotificationService
import utils.{ SecureAction, CHErrorHandler }

@Singleton
class NotificationController @Inject()(
  val controllerComponents: ControllerComponents,
  notifService: NotificationService,
  SecureAction: SecureAction
)(implicit ec: ExecutionContext) extends BaseController {

  def index = SecureAction.async {
    notifService.getNotifications.map(notifications => Ok(Json.obj("notifications" -> Json.toJson(notifications))))
  }

  def create = SecureAction.async(parse.json) { request =>
    val json = request.body.validate[Notification]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
      notification => {
        notifService.createNotification(notification).fold(CHErrorHandler(_), success => Created(Json.obj("success" -> s"$success")))
      }
    )
  }

  def edit(id: UUID) = SecureAction.async {
    notifService.editNotification(id).fold(CHErrorHandler(_), success => Created(Json.obj("success" -> s"$success")))
  }
}