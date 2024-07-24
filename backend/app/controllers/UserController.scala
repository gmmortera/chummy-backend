package controllers

import javax.inject.{ Singleton, Inject }

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.User
import models.service.UserService
import utils.CHErrorHandler

@Singleton
class UserController @Inject()(
  userService: UserService, 
  val controllerComponents: ControllerComponents)
  (implicit ec: ExecutionContext) extends BaseController with I18nSupport {

    def create = Action.async(parse.json) { implicit request =>
      val json = request.body.validate[User]
      json.fold(
        error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
        user => {
          userService.createUser(user)
            .fold(CHErrorHandler(_), success => Created(Json.obj("messsage" -> s"$success")))
        }
      )
    }
}