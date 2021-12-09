import statistic.EventStatisticImpl
import time.RealTimeManager

fun main() {
    val eventStatistic = EventStatisticImpl(RealTimeManager())

    while (true) {
        val line = readLine()?.trim() ?: break

        val splitted = line.split(" ")

        try {
            when (splitted[0]) {
                "inc" -> eventStatistic.incEvent(splitted[1]).also { println(it) }
                "getByName" -> eventStatistic.getEventStatisticByName(splitted[1]).also { println(it) }
                "getAll" -> eventStatistic.getAllEventStatistic().also { println(it) }
                "print" -> eventStatistic.printStatistic()
                "exit" -> break
            }
        } catch (e: Exception) {
            continue
        }
    }
}