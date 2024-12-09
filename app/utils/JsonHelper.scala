package utils

import java.util.UUID

import play.api.libs.json._

object JsonHelper {
  def transformJson(json: JsValue, idUser: UUID): JsValue = {
    val transformer = JsPath.json.update((JsPath \ "idUser").json.put(JsString(idUser.toString)))
    json.transform(transformer).getOrElse(json)
  }
}
