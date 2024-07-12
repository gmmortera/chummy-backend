package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.LoginData
import models.service.UserService

@Singleton
class AuthController @Inject()(
  userService: UserService,
  val controllerComponents: ControllerComponents)
  (implicit ec: ExecutionContext) extends BaseController with I18nSupport {
  
  def create() = Action.async(parse.json) { implicit request =>
    val json = request.body.validate[LoginData]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
      data => {
        userService.validateUser(data).value.map {
          case Left(error) => error.toResult
          case Right(success) => Ok(
            Json.obj("user" -> Json.toJson(success)))
            .withSession("authenticated" -> success.id.toString)
        }
      }
    )
  }

  def destroy() = Action.async { request =>
    Future.successful(Ok.withNewSession)
  }
}