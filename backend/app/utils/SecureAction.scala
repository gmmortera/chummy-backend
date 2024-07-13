package utils

import play.api.mvc._
import play.api.mvc.Results._

import java.util.UUID
import javax.inject._

import scala.concurrent.{ ExecutionContext, Future }

import utils.CHError
import scala.util.Try

class SecureAction @Inject()(
  val parser: BodyParsers.Default,
  implicit val executionContext: ExecutionContext
) extends ActionBuilder[UserSessionRequest, AnyContent] with CHError {
  override def invokeBlock[A](
    request: Request[A], 
    block: UserSessionRequest[A] => Future[Result]
  ) = { 
    request.session.get("authenticated").flatMap { implicit id =>
      Try(UUID.fromString(id)).toOption
    }.fold(
      Future.successful(Unauthorized("Unauthorized access"))
    )(id => block(UserSessionRequest(id, request)))
  }
}

case class UserSessionRequest[A](
  idSession: UUID, 
  request: Request[A]
) extends WrappedRequest[A](request)