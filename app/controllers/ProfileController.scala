package controllers

import javax.inject._
import java.util.UUID

import play.api._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.{ Future, ExecutionContext }

import models.domain.Profile
import models.service.ProfileService
import utils.{ SecureAction, CHErrorHandler }

@Singleton
class ProfileController @Inject()(
  val controllerComponents: ControllerComponents,
  profileService: ProfileService,
  SecureAction: SecureAction
)(implicit ec: ExecutionContext) extends BaseController {

  def index = SecureAction.async {
    profileService.getProfiles.map(profiles => Ok(Json.toJson(profiles)))  
  }

  def create = SecureAction.async(parse.json) { request =>
    val json = request.body.validate[Profile]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
      profile => {
        profileService.createProfile(profile).fold(CHErrorHandler(_), success => Created(Json.obj("success" -> s"$success")))
      }
    )
  }

  def edit = SecureAction.async(parse.json) { request =>
    val json = request.body.validate[Profile]
    json.fold(
      error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
      profile => {
        profileService.editProfile(profile).fold(CHErrorHandler(_), success => Ok(Json.obj("success" -> s"$success")))
      }
    )
  }
}