package service

import domain.ActivityType.ENTRANCE
import event.ActivityOccurredEvent
import event.Event
import event.parseEventDocuments
import listener.EventListener
import org.bson.types.ObjectId
import java.time.Duration
import java.util.*
import kotlin.math.round

class ReportService(
    private val databaseService: DatabaseService
) : EventListener() {
    private val localStorage = mutableListOf<Event>()

    init {
        databaseService.findAll().parseEventDocuments().forEach { localStorage.add(it) }

        databaseService.listenDocuments(this)
    }

    override fun notifyEvent(event: Event) {
        localStorage.add(event)
    }

    fun getVisitCountByDayOfWeek(): List<Int> {
        val dayOfWeekCounter = mutableListOf<Int>().apply {
            repeat(7) {
                this.add(0)
            }
        }

        localStorage.filter { event ->
            return@filter when (event) {
                is ActivityOccurredEvent -> {
                    event.activityType == ENTRANCE
                }

                else -> false
            }
        }.forEach {
            val calendar = Calendar.getInstance()
            calendar.time = it.timestamp

            dayOfWeekCounter[calendar.firstDayOfWeek - 1]++
        }

        return dayOfWeekCounter
    }

    fun getAvgVisitFrequency(): Double {
        val clientIdVisitCountMap = mutableMapOf<ObjectId, Int>()

        localStorage.filter { event ->
            return@filter when (event) {
                is ActivityOccurredEvent -> {
                    event.activityType == ENTRANCE
                }

                else -> false
            }
        }.forEach {
            val clientId = (it as ActivityOccurredEvent).clientId
            if (!clientIdVisitCountMap.containsKey(clientId)) {
                clientIdVisitCountMap[clientId] = 0
            }

            clientIdVisitCountMap[clientId] = clientIdVisitCountMap[clientId]!! + 1
        }

        return clientIdVisitCountMap.values.sum().toDouble() / clientIdVisitCountMap.keys.count().toDouble()
    }

    fun getAvgVisitDuration(): Duration? {
        val clientIdListActivityEventMap = mutableMapOf<ObjectId, MutableList<ActivityOccurredEvent>>()

        val visitDurationList = mutableListOf<Long>()

        localStorage.filter { event ->
            return@filter when (event) {
                is ActivityOccurredEvent -> true

                else -> false
            }
        }.forEach {
            val clientId = (it as ActivityOccurredEvent).clientId
            if (!clientIdListActivityEventMap.containsKey(clientId)) {
                clientIdListActivityEventMap[clientId] = mutableListOf()
            }

            clientIdListActivityEventMap[clientId]!!.add(it)
        }

        for (clientId in clientIdListActivityEventMap.keys) {
            clientIdListActivityEventMap[clientId]!!.sortBy { event ->
                event.timestamp
            }

            var start = 0L
            clientIdListActivityEventMap[clientId]!!.forEach { event ->
                if (event.activityType == ENTRANCE) {
                    start = event.timestamp.time
                } else {
                    visitDurationList.add(event.timestamp.time - start)
                }
            }
        }

        return Duration.ofMillis(
            round(
                visitDurationList.sum().toDouble() / visitDurationList.count().toDouble()
            ).toLong()
        )
    }
}