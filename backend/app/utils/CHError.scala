package utils

import play.api.mvc.Results
import play.api.http.Status

case class ServiceError(
  status: Int,
  code: String,
  cause: Throwable = null
) extends Exception(code, cause) {
  def toResult = Results.Status(status)(code)
}

trait CHError {
  val USER_ERROR_REGISTER = new ServiceError(Status.FORBIDDEN, "user.error.register")
  val USER_ERROR_LOGIN = new ServiceError(Status.UNAUTHORIZED, "user.error.login")
  val AUTH_ERROR = new ServiceError(Status.UNAUTHORIZED, "auth.error")
}