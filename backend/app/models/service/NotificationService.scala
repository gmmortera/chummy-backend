package models.service

import javax.inject.{ Singleton, Inject }
import java.util.UUID
import java.time.Instant

import play.api.http.Status

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.EitherT
import cats.syntax.all._

import models.domain.Notification
import models.repo.NotificationRepo
import utils.CHError
import utils.result.CHResult

@Singleton
class NotificationService @Inject()(notifRepo: NotificationRepo)(implicit ec: ExecutionContext) {
  def getNotifications: Future[Seq[Notification]] = notifRepo.notifications.get
  def createNotification(notification: Notification): CHResult[String] = EitherT {
    val query = notifRepo.notifications.create(notification)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "notif.error.create")),
      _ => Right("Notification added successfully")
    )}
  }
  def editNotification(id: UUID): CHResult[String] = EitherT {
    val query = notifRepo.notifications.edit(id)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "notif.error.edit")),
      _ => Right("Notification edited successfully")
    )}
  }
}
