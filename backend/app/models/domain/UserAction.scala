package models.domain

import java.util.UUID

import play.api.libs.json._
import play.api.mvc.WebSocket.MessageFlowTransformer

import org.apache.pekko.actor.ActorRef

case class UserAction(action: String, data: JsValue)
case class ActorData(id: UUID, ref: ActorRef)

object UserAction {
  implicit val userActionFormat: Format[UserAction] = Json.format[UserAction]

  implicit val messageFlowTransformer: MessageFlowTransformer[UserAction, UserAction] =
    MessageFlowTransformer.jsonMessageFlowTransformer[UserAction, UserAction]
}
