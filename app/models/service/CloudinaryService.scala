package models.service

import javax.inject.{ Singleton, Inject }
import java.nio.file.Files
import java.util.UUID

import play.api.http.Status
import play.api.libs.ws._
import play.api.mvc._
import play.api.mvc.MultipartFormData._
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json._

import scala.concurrent.{ Future, ExecutionContext }

import com.typesafe.config.{ Config, ConfigFactory }
import org.apache.pekko.util.ByteString
import org.apache.pekko.stream.scaladsl._
import org.apache.pekko.stream.IOResult
import cats.data.EitherT
import cats.syntax.all._

import utils.result.CHResult
import utils.CHError

@Singleton
class CloudinaryService @Inject()(
  ws: WSClient
)(implicit ec: ExecutionContext) {
  val config: Config = ConfigFactory.load()
  val apiKey = config.getString("cloudinary.api-key")
  val baseUrl = config.getString("cloudinary.url")

  def createUpload(image: FilePart[Source[ByteString, Future[IOResult]]]): CHResult[(String, String)] = EitherT {
    val request: WSRequest = ws.url(baseUrl + "upload")
    val formData = Source(
      image :: DataPart("api_key", apiKey) :: DataPart("upload_preset", "chummy") :: List()
    )

    request.post(formData).map { res =>
      val url = Json.parse(res.body)("url").as[String]
      val signature = Json.parse(res.body)("signature").as[String]

      Either.cond(res.status == 200, (url, signature), CHError(Status.BAD_REQUEST, "image.error.upload"))
    }
  }

  def destroyUpload(publicId: String, signature: String): CHResult[String] = EitherT {
    val request: WSRequest = ws.url(baseUrl + "destroy")
    val data = Map(
      "public_id" -> Seq(publicId),
      "api_key" -> Seq(apiKey),
      "signature" -> Seq(signature)
    )

    request.post(data).map { res =>
        Either.cond(res.status == 200, "Image deleted", CHError(Status.BAD_REQUEST, "image.error.delete"))
      }
    }
}