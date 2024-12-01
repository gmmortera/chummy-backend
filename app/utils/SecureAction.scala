package utils

import play.api.mvc._
import play.api.mvc.Results._
import play.api.http.Status

import java.util.UUID
import javax.inject._

import scala.concurrent.{ ExecutionContext, Future }

import utils.{ CHError, CHErrorHandler }
import scala.util.Try

class SecureAction @Inject()(
  val parser: BodyParsers.Default,
  implicit val executionContext: ExecutionContext
) extends ActionBuilder[UserSessionRequest, AnyContent] {
  override def invokeBlock[A](
    request: Request[A], 
    block: UserSessionRequest[A] => Future[Result]
  ) = { 
    request.session.get("authenticated").flatMap { implicit id =>
      Try(UUID.fromString(id)).toOption
    }.fold(
      Future.successful(CHErrorHandler(CHError(Status.UNAUTHORIZED, "auth.error")))
    )(id => block(UserSessionRequest(id, request)))
  }
}

case class UserSessionRequest[A](
  idSession: UUID, 
  request: Request[A]
) extends WrappedRequest[A](request)