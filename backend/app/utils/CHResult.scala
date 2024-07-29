package utils

import scala.concurrent.Future

import cats.data.EitherT
import cats.syntax.all._

import utils.CHError

object result {
  type CHResult[A] = EitherT[Future, CHError, A]
}