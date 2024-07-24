package utils

import play.api.mvc.Results
import play.api.http.Status

case class CHError(
  status: Int,
  code: String,
  cause: Throwable = null
) extends Exception(code, cause)

object CHErrorHandler {
  def apply(error: CHError) = {
    Results.Status(error.status)(error.code)
  }
}