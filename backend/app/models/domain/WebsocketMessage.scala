package models.domain

import play.api.libs.json._
import play.api.mvc.WebSocket.MessageFlowTransformer

sealed trait WebsocketMessage
case class UserAction(action: String, data: JsValue) extends WebsocketMessage

object WebsocketMessage {
  implicit val userActionFormat: Format[UserAction] = Json.format[UserAction]

  implicit val websocketMessageReads: Reads[WebsocketMessage] = Json.reads[UserAction].map(ua => ua: WebsocketMessage)

  implicit val websocketMessageWrites: Writes[WebsocketMessage] = Writes {
    case userAction: UserAction => Json.toJson(userAction)
  }

  implicit val messageFlowTransformer: MessageFlowTransformer[WebsocketMessage, WebsocketMessage] =
    MessageFlowTransformer.jsonMessageFlowTransformer[WebsocketMessage, WebsocketMessage]
}
