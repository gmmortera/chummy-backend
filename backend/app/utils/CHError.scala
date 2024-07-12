package utils

import play.api.mvc.Results
import play.api.http.Status
import play.api.i18n.Messages

case class ServiceError(
  status: Int,
  code: String,
  cause: Throwable = null
) extends Exception(code, cause) {
  def toResult(implicit messages: Messages) = Results.Status(status)(messages(code))
}

trait CHError {
  val USER_ERROR_REGISTER = new ServiceError(Status.FORBIDDEN, "user.error.register")
  val USER_ERROR_LOGIN = new ServiceError(Status.UNAUTHORIZED, "user.error.login")
}