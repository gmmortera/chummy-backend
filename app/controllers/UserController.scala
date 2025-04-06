package controllers

import javax.inject.{ Singleton, Inject }

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.User
import models.service.UserService
import utils.{ CHErrorHandler, SecureAction }

@Singleton
class UserController @Inject()(
  userService: UserService,
  SecureAction: SecureAction,
  val controllerComponents: ControllerComponents)
  (implicit ec: ExecutionContext) extends BaseController with I18nSupport {
    def index = SecureAction.async {
      userService.getUsers.map(users => Ok(Json.obj("users" -> Json.toJson(users))))
    }

    def create = Action.async(parse.json) { implicit request =>
      val json = request.body.validate[User]
      json.fold(
        error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
        user => {
          userService.createUser(user)
            .fold(CHErrorHandler(_), success => Created(Json.toJson(user)))
        }
      )
    }
}