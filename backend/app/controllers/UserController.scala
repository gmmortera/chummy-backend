package controllers

import javax.inject.{ Singleton, Inject }

import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.User
import models.service.UserService

@Singleton
class UserController @Inject()(
  userService: UserService, 
  val controllerComponents: ControllerComponents)
  (implicit ec: ExecutionContext) extends BaseController {

    def create() = Action.async(parse.json) { request =>
      val json = request.body.validate[User]
      json.fold(
        error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
        user => {
          userService.createUser(user).value.map {
            case Left(error) => Unauthorized(Json.obj("messsage" -> s"$error"))
            case Right(success) => Created(Json.obj("messsage" -> s"$success"))
          }
        }
      )
    }
}