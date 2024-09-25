package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.i18n._
import play.api.libs.streams.ActorFlow

import scala.concurrent.{ Future, ExecutionContext }

import org.apache.pekko.actor.{ ActorSystem, Props, ActorRef }
import org.apache.pekko.stream.Materializer
import org.apache.pekko.stream.scaladsl._

import models.domain.{ LoginData, WebsocketMessage }
import models.service.UserService
import actors._
import utils.{ CHErrorHandler, SecureAction }

@Singleton
class AuthController @Inject()(
  SecureAction: SecureAction,
  userService: UserService,
  @Named("general-manager") manager: ActorRef,
  val controllerComponents: ControllerComponents)
(implicit system: ActorSystem,
  mat: Materializer,
  val executionContext: ExecutionContext
) extends BaseController with I18nSupport {

  def index: WebSocket = WebSocket.accept[WebsocketMessage, WebsocketMessage] { implicit request =>
    ActorFlow.actorRef { out => 
      println("Websocket Entered")
      UserActor.props(out, manager)
    }
  }
  
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