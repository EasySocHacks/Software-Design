package actor.search.master

import actor.search.child.QuerySearchChildActor
import akka.actor.*
import akka.japi.pf.DeciderBuilder
import domain.QueryResponse
import exception.ActorIsBusyException
import exception.UnsupportedActorMessageTypeException
import java.time.Duration
import kotlin.math.max

@Suppress("UNCHECKED_CAST")
class QuerySearchMasterActor(
    private val querySearchChildActorClassList: List<Pair<Class<out QuerySearchChildActor>, List<Any?>>>,
    private val timeout: Long = 5000L
) : UntypedAbstractActor() {
    init {
        context.receiveTimeout = Duration.ofMillis(timeout)
    }

    private var startWaitingTileMillis: Long = -1L
    private var answerCount = 0

    private fun updateReceiveTimeout() {
        context.receiveTimeout = Duration.ofMillis(
            max(
                0,
                timeout - (System.currentTimeMillis() - startWaitingTileMillis)
            )
        )
    }

    override fun onReceive(message: Any?) {
        when (message) {
            is String -> {
                if (startWaitingTileMillis != -1L) {
                    throw ActorIsBusyException("Actor is currently awaiting for previous query search results")
                }

                for (querySearchChildActorPairClassArgs in querySearchChildActorClassList) {
                    context.actorOf(
                        Props.create(
                            querySearchChildActorPairClassArgs.first,
                            *querySearchChildActorPairClassArgs.second.toTypedArray()
                        )
                    )
                        .tell(message, self)
                }

                startWaitingTileMillis = System.currentTimeMillis()
            }

            is List<*> -> {
                println((message as List<QueryResponse>).joinToString(System.lineSeparator()))

                updateReceiveTimeout()

                context.stop(sender)

                answerCount++
                if (answerCount == querySearchChildActorClassList.size) {
                    println("Done!")
                    context.stop(self)
                }
            }

            is ReceiveTimeout -> {
                println("Time's out")
                context.stop(self)
            }

            else ->
                throw UnsupportedActorMessageTypeException("Message with type ${message?.javaClass} is unsupported to process with current actor")
        }
    }

    override fun supervisorStrategy(): SupervisorStrategy {
        return OneForOneStrategy(
            false, DeciderBuilder
                .match(ActorIsBusyException::class.java) { OneForOneStrategy.resume() }
                .match(UnsupportedActorMessageTypeException::class.java) { OneForOneStrategy.stop() }
                .build()
        )
    }
}