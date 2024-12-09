package controllers

import javax.inject._
import java.util.UUID

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.mvc.MultipartFormData._


import scala.concurrent.{ Future, ExecutionContext }

import org.apache.pekko.stream.scaladsl._

import models.domain.{ Post, Asset }
import models.service.{ PostService, CloudinaryService }
import utils._

@Singleton
class PostController @Inject()(
  val controllerComponents: ControllerComponents,
  postService: PostService,
  cloudinaryService: CloudinaryService,
  SecureAction: SecureAction
)(implicit ec: ExecutionContext) extends  BaseController {

  def index = SecureAction.async {
    postService.getPosts.map(posts => Ok(Json.obj("posts" -> Json.toJson(posts))))
  }

  def create = SecureAction.async(parse.multipartFormData) { implicit request =>
    val jsonBody = request.body.asFormUrlEncoded.get("json").flatMap(_.headOption)
    val parsedJson = Json.parse(jsonBody.getOrElse("{}"))
    val file = request.body.file("file")
    val json = request.session.get("authenticated").map(id => {
        JsonHelper.transformJson(parsedJson, UUID.fromString(id))
      }
    ).getOrElse(parsedJson)

    file match {
      case Some(data) =>
        val image = FilePart("file", data.filename, data.contentType, FileIO.fromPath(data.ref.path))
        cloudinaryService.createUpload(image).foldF(
          error => Future.successful(CHErrorHandler(error)),
          asset => json.validate[Post].fold(
            error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
            post => {
              val withImage = post.addImage(asset._1, asset._2)
              postService.createPost(withImage).fold(CHErrorHandler(_), success => Created(Json.obj("post" -> Json.toJson(withImage))))
            }
          )
        )
      case None =>
        json.validate[Post].fold(
          error => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(error)))),
          post => {
            postService.createPost(post).fold(CHErrorHandler(_), success => Created(Json.obj("post" -> Json.toJson(post))))
          }
        )
    }
  }

  def destroy(id: UUID) = SecureAction.async(parse.json) { implicit request =>
    val asset = request.body.validate[Asset]

    asset.fold(
      error => Future.successful(BadRequest(Json.obj("errors" -> JsError.toJson(error)))),
      imageInfo => imageInfo match {
        case Asset(Some(publicId), Some(signature)) => cloudinaryService.destroyUpload(publicId, signature).foldF(
          error => Future.successful(CHErrorHandler(error)),
          success => postService.deletePost(id).fold(CHErrorHandler(_), success => Ok(Json.obj("success" -> s"$success")))
        )
        case Asset(None, None) => postService.deletePost(id).fold(CHErrorHandler(_), success => Ok(Json.obj("success" -> s"$success")))
      }
    )
  }
}