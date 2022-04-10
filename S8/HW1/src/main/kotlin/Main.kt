import actor.search.child.GoogleQuerySearchChildActor
import actor.search.child.QuerySearchChildActor
import actor.search.child.YandexQuerySearchChildActor
import actor.search.master.QuerySearchMasterActor
import akka.actor.ActorRef.noSender
import akka.actor.ActorSystem
import akka.actor.Props

fun main(args: Array<String>) {
    val query = "123test"

    val actorSystem = ActorSystem.create()

    actorSystem.actorOf(
        Props.create(
            QuerySearchMasterActor::class.java,
            listOf<Pair<Class<out QuerySearchChildActor>, List<Any?>>>(
                Pair(YandexQuerySearchChildActor::class.java, emptyList()),
                Pair(GoogleQuerySearchChildActor::class.java, emptyList())
            ),
            60000L
        )
    ).tell(query, noSender())
}