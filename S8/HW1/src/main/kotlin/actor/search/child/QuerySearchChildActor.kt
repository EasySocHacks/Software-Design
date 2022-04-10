package actor.search.child

import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy
import akka.actor.UntypedAbstractActor
import akka.japi.pf.DeciderBuilder
import domain.QueryResponse
import exception.UnsupportedActorMessageTypeException

abstract class QuerySearchChildActor : UntypedAbstractActor() {
    abstract fun sendSearchQuery(queryText: String): String

    abstract fun proceedResponse(html: String, limit: Int = 5): List<QueryResponse>

    override fun onReceive(message: Any?) {
        when (message) {
            is String -> {
                val html = sendSearchQuery(message)
                val queryResponseList = proceedResponse(html)

                sender().tell(queryResponseList, self)
            }
            else ->
                throw UnsupportedActorMessageTypeException("Message with type ${message?.javaClass} is unsupported to process with current actor")
        }
    }

    override fun supervisorStrategy(): SupervisorStrategy {
        return OneForOneStrategy(
            false, DeciderBuilder
                .match(UnsupportedActorMessageTypeException::class.java) { OneForOneStrategy.stop() }
                .build()
        )
    }
}