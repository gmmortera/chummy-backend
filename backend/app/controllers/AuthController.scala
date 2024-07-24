package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.LoginData
import models.service.UserService
import utils.{ CHErrorHandler, SecureAction }

@Singleton
class AuthController @Inject()(
  SecureAction: SecureAction,
  userService: UserService,
  val controllerComponents: ControllerComponents)
  (implicit ec: ExecutionContext) extends BaseController with I18nSupport {
  
  def create = Action.async(parse.json) { implicit request =>
    val json = request.body.validate[LoginData]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
      data => {
        userService.validateUser(data).fold(
          CHErrorHandler(_), 
          success => Ok(Json.obj("user" -> Json.toJson(success))).withSession("authenticated" -> success.id.toString))
      }
    )
  }

  def destroy = SecureAction.async(parse.json) { implicit request =>
    Future.successful(Ok.withNewSession)
  }
}