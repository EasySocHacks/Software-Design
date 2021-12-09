package statistic

import time.FakeTimeManager.Companion.withFrozenTime
import time.TimeManager
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ConcurrentSkipListSet

class EventStatisticImpl(
    private val timeManager: TimeManager
) : EventStatistic {
    private val eventMap: ConcurrentMap<String, ConcurrentSkipListSet<Date>> = ConcurrentHashMap()

    override fun incEvent(name: String): Boolean = with(timeManager) {
        eventMap.getOrPut(name) {
            ConcurrentSkipListSet<Date>(kotlin.Comparator { o1, o2 ->
                return@Comparator o1.compareTo(o2)
            })
        }.add(now())
    }

    override fun getEventStatisticByName(name: String): Double = withFrozenTime(
        GregorianCalendar(
            timeManager.get(YEAR),
            timeManager.get(MONTH),
            timeManager.get(DAY_OF_MONTH),
            timeManager.get(HOUR_OF_DAY),
            timeManager.get(MINUTE),
            timeManager.get(SECOND)
        )
    ) {
        val hourAgo = add(HOUR_OF_DAY, -1).now()

        return@withFrozenTime eventMap[name]?.filter { date ->
            return@filter date >= hourAgo && date <= now()
        }?.count()?.toDouble()?.div(60.0) ?: 0.0
    }

    override fun getAllEventStatistic(): Map<String, Double> = withFrozenTime(
        GregorianCalendar(
            timeManager.get(YEAR),
            timeManager.get(MONTH),
            timeManager.get(DAY_OF_MONTH),
            timeManager.get(HOUR_OF_DAY),
            timeManager.get(MINUTE),
            timeManager.get(SECOND)
        )
    ) {
        val hourAgo = add(HOUR_OF_DAY, -1).now()

        @Suppress("UNCHECKED_CAST")
        return@withFrozenTime eventMap.mapValues { entry ->
            entry.value.filter { date ->
                return@filter date >= hourAgo && date <= now()
            }.count().let {
                if (it == 0) null
                else it
            }?.toDouble()?.div(60.0)
        }.filter { entry ->
            entry.value != null
        } as Map<String, Double>
    }

    override fun printStatistic() = withFrozenTime(
        GregorianCalendar(
            timeManager.get(YEAR),
            timeManager.get(MONTH),
            timeManager.get(DAY_OF_MONTH),
            timeManager.get(HOUR_OF_DAY),
            timeManager.get(MINUTE),
            timeManager.get(SECOND)
        )
    ) {
        eventMap.map { entry ->
            (((now().time) - (entry.value.minOrNull()?.time ?: now().time)) / 60000L).also { minutes ->
                print("${entry.key}: ")
                if (minutes == 0L) {
                    println("0.0")
                } else {
                    println("${entry.value.count().toDouble() / minutes}")
                }
            }
        }

        Unit
    }
}