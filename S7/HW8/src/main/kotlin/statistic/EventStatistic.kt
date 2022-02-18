package statistic

interface EventStatistic {
    fun incEvent(name: String): Boolean

    fun getEventStatisticByName(name: String): Double
    fun getAllEventStatistic(): Map<String, Double>

    fun printStatistic()
}