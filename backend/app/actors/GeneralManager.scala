package actors

import play.api.libs.json._

import org.apache.pekko.actor.{ Actor, ActorRef, Props }

import models.domain.WebsocketMessage

class GeneralManager extends Actor {
  import GeneralManager._

  def receive: Receive = init(Seq.empty[ActorRef])

  def init(users: Seq[ActorRef]): Receive = {
    case NewUser(user) => context.become(init(users :+ user))
    case NewPost(user, post) => for (u <- users) {
      if (u != user) u ! UserActor.SendUpdate(post)
    }
    case NewComment(user, comment) => for (u <- users) {
      if (u != user) u ! UserActor.SendUpdate(comment)
    }
    case NewState(user, like) => for (u <- users) {
      if (u != user) u ! UserActor.SendUpdate(like)
    }
  }
}

object GeneralManager {
  def props = Props(new GeneralManager)

  case class NewUser(user: ActorRef)
  case class NewPost(user: ActorRef, post: WebsocketMessage)
  case class NewComment(user: ActorRef, comment: WebsocketMessage)
  case class NewState(user: ActorRef, like: WebsocketMessage)
}