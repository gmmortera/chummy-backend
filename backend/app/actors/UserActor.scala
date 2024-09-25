package actors

import play.api.libs.json._

import org.apache.pekko.actor.{ Actor, ActorRef, Props }

import models.domain.{ WebsocketMessage, UserAction }

class UserActor(out: ActorRef, manager: ActorRef) extends Actor {
  import UserActor._
  manager ! GeneralManager.NewUser(self)

  def receive: Receive = {
    case msg: WebsocketMessage => handleMessage(msg)
    case SendUpdate(res) => 
      println(s"User: $self")
      out ! res
    case _ => println("Unhandled message")
  }

  def handleMessage(msg: WebsocketMessage): Unit = msg match {
    case UserAction("post", _) => manager ! GeneralManager.NewPost(self, msg)
    case UserAction("comment", _) => manager ! GeneralManager.NewComment(self, msg)
    case UserAction("like", _) => manager ! GeneralManager.NewState(self, msg)
    case _ => println(s"Unhandled websocket message $msg")
  }
}

object UserActor {
  def props(out: ActorRef, manager: ActorRef) = Props(new UserActor(out, manager))

  case class SendUpdate(res: WebsocketMessage)
}
