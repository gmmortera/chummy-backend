package models.service

import javax.inject.{ Singleton, Inject }
import java.util.UUID

import play.api.http.Status

import scala.concurrent.{ Future, ExecutionContext }

import cats.data.EitherT
import cats.syntax.all._

import models.domain.Profile
import models.repo.ProfileRepo
import utils.CHError
import utils.result.CHResult

@Singleton
class ProfileService @Inject()(
  profileRepo: ProfileRepo
)(implicit ec: ExecutionContext) {
  def getProfiles: Future[Seq[Profile]] = profileRepo.profiles.get

  def createProfile(profile: Profile): CHResult[String] = EitherT {
    val query = profileRepo.profiles.create(profile)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "profile.error.create")),
      _ => Right("Profile added successfully")
    )}
  }

  def editProfile(profile: Profile): CHResult[String] = EitherT {
    val query = profileRepo.profiles.edit(profile)
    query.map { _.fold(
      _ => Left(CHError(Status.BAD_REQUEST, "profile.error.edit")),
      _ => Right("Profile edited successfully")
    )}
  }
}