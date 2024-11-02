package modules
import com.google.inject.AbstractModule
import play.api.libs.concurrent.PekkoGuiceSupport
import javax.inject._

@Singleton
class ActorModule extends AbstractModule with PekkoGuiceSupport {
	import actors.GeneralManager
	override def configure = {
		bindActor[GeneralManager]("general-manager")
	}
}