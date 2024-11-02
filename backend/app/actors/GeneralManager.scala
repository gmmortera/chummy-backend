package actors

import java.util.UUID

import play.api.libs.json._

import org.apache.pekko.actor.{ Actor, ActorRef, Props }

import models.domain._

class GeneralManager extends Actor {
  import GeneralManager._

  def receive: Receive = init(Seq.empty[ActorData])

  def init(users: Seq[ActorData]): Receive = {
    case NewUser(user) => context.become(init(users :+ user))
    case NewPost(id, post) => for (u <- users) {
      if (u.id != id) u.ref ! UserActor.SendUpdate(post)
    }
    case NewComment(id, comment) => for (u <- users) {
      if (u.id != id) u.ref ! UserActor.SendUpdate(comment)
    }
    case NewLike(id, like) => for (u <- users) {
      if (u.id != id) u.ref ! UserActor.SendUpdate(like)
    }
  }
}

object GeneralManager {
  def props = Props(new GeneralManager)

  case class NewUser(user: ActorData)
  case class NewPost(id: UUID, post: UserAction)
  case class NewComment(id: UUID, comment: UserAction)
  case class NewLike(id: UUID, like: UserAction)
}