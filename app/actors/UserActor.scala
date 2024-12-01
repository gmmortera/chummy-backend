package actors

import java.util.UUID

import play.api.libs.json._

import org.apache.pekko.actor.{ Actor, ActorRef, Props }

import models.domain._

class UserActor(id: UUID, out: ActorRef, manager: ActorRef) extends Actor {
  import UserActor._

  val user = ActorData(id, self)
  manager ! GeneralManager.NewUser(user)

  def receive: Receive = {
    case msg: UserAction => handleMessage(msg)
    case SendUpdate(res) => out ! res
    case _ => println("Unhandled message")
  }

  def handleMessage(msg: UserAction): Unit = msg.action match {
    case "new-post" => manager ! GeneralManager.NewPost(id, msg)
    case "new-comment" => manager ! GeneralManager.NewComment(id, msg)
    case "new-like" => manager ! GeneralManager.NewLike(id, msg)
    case _ => println(s"Unhandled websocket message $msg")
  }
}

object UserActor {
  def props(id: UUID, out: ActorRef, manager: ActorRef) = Props(new UserActor(id, out, manager))

  case class SendUpdate(res: UserAction)
}
